package com.github.vahundos.breezer.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vahundos.breezer.TestData;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.stream.Stream;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql(scripts = "/init-data.sql")
class UserRestControllerIT {

    private static final String BASE_URL = "/users/";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Test
    void get_returnsUser_WhenUserExists() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "1").contentType(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                                     .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        User expectedUser = TestData.getUser1();
        expectedUser.setPassword(null);
        JSONAssert.assertEquals(objectMapper.writeValueAsString(expectedUser), responseBody, false);
    }

    @Test
    void get_returnsNotFoundResponse_WhenUserDoesntExist() throws Exception {
        var id = 4L;
        mockMvc.perform(get(BASE_URL + id).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.message", equalTo(String.format("User with id=%d not found", id))))
               .andReturn();
    }

    @Test
    void register_createsAndReturnsRegisteredUserWithId() throws Exception {
        mockMvc.perform(post(BASE_URL + "register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(TestData.getUserForRegistration())))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id", equalTo(3)));
    }

    @ParameterizedTest
    @MethodSource("provideNotWellFormedUserFields")
    void register_returnsBadRequestResponse_WhenRequestBodyIsNotWellFormed(UserRegistrationDto notWellFormedUser, String fieldName) throws Exception {
        mockMvc.perform(post(BASE_URL + "register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(notWellFormedUser)))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$[0].fieldName", equalTo(fieldName))) ;
    }

    private static Stream<Arguments> provideNotWellFormedUserFields() {
        return Stream.of(
                Arguments.of(new UserRegistrationDto(null, "secondName", "nickname", "email@mail.net", "password"), "firstName"),
                Arguments.of(new UserRegistrationDto("firstName", null, "nickname", "email@mail.net", "password"), "secondName"),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", null, "email@mail.net", "password"), "nickname"),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", null, "password"), "email"),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", "email@mail.net", null), "password"),
                Arguments.of(new UserRegistrationDto("", "secondName", "nickname", "email@mail.net", "password"), "firstName"),
                Arguments.of(new UserRegistrationDto("firstName", "", "nickname", "email@mail.net", "password"), "secondName"),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "", "email@mail.net", "password"), "nickname"),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", "", "password"), "email"),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", "email@mail.net", ""), "password"),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", "email", "password"), "email"),

                // email already exists
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", "ivan.ivanov@mail.net", "password"), "email"),

                // nickname already exists
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "ivanivanov", "email@mail.net", "password"), "nickname"),

                Arguments.of(new UserRegistrationDto("firstName", "secondName", "ivan_not_well_formed", "email@mail.net", "password"), "nickname")
        );
    }

    @Test
    void activate_changesUserStatusToActivated() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + "1/activate").contentType(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        User expectedUser = TestData.getUser1();
        expectedUser.setPassword(null);
        expectedUser.setStatus(UserStatus.ACTIVATED);
        JSONAssert.assertEquals(objectMapper.writeValueAsString(expectedUser), responseBody, false);
    }

    @Test
    void activate_returnsBadRequestResponse_WhenIdIsNotWellFormed() throws Exception {
        mockMvc.perform(put(BASE_URL + "abc/activate").contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void activate_returnsNotFoundResponse_WhenUserDoesntExist() throws Exception {
        mockMvc.perform(put(BASE_URL + "4/activate").contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andReturn();
    }

    @Test
    void ban_changeUserStatusToBanned() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + "1/ban").contentType(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        User expectedUser = TestData.getUser1();
        expectedUser.setPassword(null);
        expectedUser.setStatus(UserStatus.BANNED);
        JSONAssert.assertEquals(objectMapper.writeValueAsString(expectedUser), responseBody, false);
    }

    @Test
    void ban_returnsBadRequestResponse_WhenIdIsNotWellFormed() throws Exception {
        mockMvc.perform(put(BASE_URL + "abc/ban").contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void ban_returnsNotFoundResponse_WhenUserDoesntExist() throws Exception {
        mockMvc.perform(put(BASE_URL + "4/ban").contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andReturn();
    }
}