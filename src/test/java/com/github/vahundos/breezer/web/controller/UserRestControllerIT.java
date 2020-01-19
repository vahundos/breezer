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
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "1"))
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
        mockMvc.perform(get(BASE_URL + id))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.message", equalTo("Entity with id = " + id + " not found")))
               .andReturn();
    }

    @Test
    void register_createsAndReturnsRegisteredUserWithId() throws Exception {
        mockMvc.perform(post(BASE_URL + "register")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(TestData.getUserForRegistration())))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id", equalTo(3)));
    }

    @ParameterizedTest
    @MethodSource("provideNotWellFormedUserFields")
    void register_returnsBadRequestResponse_WhenRequestBodyIsNotWellFormed(UserRegistrationDto notWellFormedUser) throws Exception {
        mockMvc.perform(post(BASE_URL + "register")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(notWellFormedUser)))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideNotWellFormedUserFields() {
        return Stream.of(
                Arguments.of(new UserRegistrationDto(null, "secondName", "nickname", "email@mail.net", "password")),
                Arguments.of(new UserRegistrationDto("firstName", null, "nickname", "email@mail.net", "password")),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", null, "email@mail.net", "password")),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", null, "password")),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", "email@mail.net", null)),
                Arguments.of(new UserRegistrationDto("", "secondName", "nickname", "email@mail.net", "password")),
                Arguments.of(new UserRegistrationDto("firstName", "", "nickname", "email@mail.net", "password")),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "", "email@mail.net", "password")),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", "", "password")),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", "email@mail.net", "")),
                Arguments.of(new UserRegistrationDto("firstName", "secondName", "nickname", "email", "password"))
        );
    }

    @Test
    void activate_changesUserStatusToActivated() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + "1/activate"))
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
        mockMvc.perform(put(BASE_URL + "abc/activate"))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void activate_returnsNotFoundResponse_WhenUserDoesntExist() throws Exception {
        mockMvc.perform(put(BASE_URL + "4/activate"))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andReturn();
    }

    @Test
    void ban_changeUserStatusToBanned() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + "1/ban"))
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
        mockMvc.perform(put(BASE_URL + "abc/ban"))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void ban_returnsNotFoundResponse_WhenUserDoesntExist() throws Exception {
        mockMvc.perform(put(BASE_URL + "4/ban"))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andReturn();
    }
}