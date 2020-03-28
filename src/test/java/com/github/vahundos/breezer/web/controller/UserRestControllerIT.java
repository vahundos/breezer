package com.github.vahundos.breezer.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vahundos.breezer.TestData;
import com.github.vahundos.breezer.UserFieldsName;
import com.github.vahundos.breezer.WellFormedUserData;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserRole;
import com.github.vahundos.breezer.model.UserStatus;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Base64;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.vahundos.breezer.TestData.*;
import static com.github.vahundos.breezer.web.controller.UserRestController.AUTH_TOKEN;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.web.authentication.www.BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql(scripts = "/init-data.sql")
class UserRestControllerIT {

    private static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";

    private static final String BASIC_AUTHENTICATION_NOT_ALLOWED_MESSAGE = "Basic authentication is supported " +
            "only for POST /users/login";

    private static final String BASE_PATH = "/users/";
    private static final String LOGIN_BASE_PATH = BASE_PATH + "login";

    private static final String MESSAGE_JSON_PATH = "$.message";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private String authToken;

    @BeforeEach
    public void setup() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(LOGIN_BASE_PATH).with(httpBasic(USERNAME, PASSWORD)))
                                          .andExpect(status().isOk())
                                          .andReturn();

        Map<String, String> responseMap = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                                 new TypeReference<>() {
                                                                 });
        this.authToken = responseMap.get(AUTH_TOKEN);
    }

    @Test
    void get_returnsUser_WhenUserExists() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(BASE_PATH + "1").contentType(MediaType.APPLICATION_JSON)
                                                                  .header(HEADER_X_AUTH_TOKEN, authToken))
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
    void get_returnsBadRequest_WhenRequestContainsBasicAuthentication() throws Exception {
        mockMvc.perform(get(BASE_PATH + "1").header(AUTHORIZATION, AUTHENTICATION_SCHEME_BASIC + " " +
                new String(Base64.getEncoder().encode("user:password".getBytes()))))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath(MESSAGE_JSON_PATH, equalTo(BASIC_AUTHENTICATION_NOT_ALLOWED_MESSAGE)));
    }

    @Test
    void get_returnsNotFoundResponse_WhenUserDoesntExist() throws Exception {
        var id = 5L;
        mockMvc.perform(get(BASE_PATH + id).contentType(MediaType.APPLICATION_JSON)
                                           .header(HEADER_X_AUTH_TOKEN, authToken))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
               .andExpect(jsonPath(MESSAGE_JSON_PATH, equalTo(String.format("User with id=%d not found", id))))
               .andReturn();
    }

    @Test
    void register_createsAndReturnsRegisteredUserWithId() throws Exception {
        mockMvc.perform(post(BASE_PATH + "register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HEADER_X_AUTH_TOKEN, authToken)
                                .content(objectMapper.writeValueAsString(TestData.getUserForRegistration())))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id", equalTo(5)))
               .andExpect(jsonPath("$.roles[0]", equalTo(UserRole.USER.name())));
    }

    @ParameterizedTest
    @MethodSource("provideNotWellFormedUserFields")
    void register_returnsBadRequestResponse_WhenRequestBodyIsNotWellFormed(UserRegistrationDto notWellFormedUser, String fieldName) throws Exception {
        mockMvc.perform(post(BASE_PATH + "register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HEADER_X_AUTH_TOKEN, authToken)
                                .content(objectMapper.writeValueAsString(notWellFormedUser)))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$[0].fieldName", equalTo(fieldName)));
    }

    private static Stream<Arguments> provideNotWellFormedUserFields() {
        return Stream.of(
                Arguments.of(new UserRegistrationDto(null, WellFormedUserData.SECOND_NAME, WellFormedUserData.NICKNAME,
                                                     WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD),
                             UserFieldsName.FIRST_NAME),

                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, null, WellFormedUserData.NICKNAME,
                                                     WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD),
                             UserFieldsName.SECOND_NAME),

                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                     null, WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD),
                             UserFieldsName.NICKNAME),

                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                     WellFormedUserData.NICKNAME, null, WellFormedUserData.PASSWORD),
                             UserFieldsName.EMAIL),

                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                     WellFormedUserData.NICKNAME, WellFormedUserData.EMAIL, null),
                             UserFieldsName.PASSWORD),

                Arguments.of(new UserRegistrationDto("", WellFormedUserData.SECOND_NAME, WellFormedUserData.NICKNAME,
                                                     WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD),
                             UserFieldsName.FIRST_NAME),

                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, "", WellFormedUserData.NICKNAME,
                                                     WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD),
                             UserFieldsName.SECOND_NAME),

                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME, "",
                                                     WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD),
                             UserFieldsName.NICKNAME),

                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                     WellFormedUserData.NICKNAME, "", WellFormedUserData.PASSWORD),
                             UserFieldsName.EMAIL),

                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                     WellFormedUserData.NICKNAME, WellFormedUserData.EMAIL, ""),
                             UserFieldsName.PASSWORD),

                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                     WellFormedUserData.NICKNAME, "not-well-formed-email",
                                                     WellFormedUserData.PASSWORD),
                             UserFieldsName.EMAIL),

                // email already exists
                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                     WellFormedUserData.NICKNAME, getUser1().getEmail(),
                                                     WellFormedUserData.PASSWORD),
                             UserFieldsName.EMAIL),

                // nickname already exists
                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME,WellFormedUserData.SECOND_NAME,
                                                     getUser1().getNickname(), WellFormedUserData.EMAIL,
                                                     WellFormedUserData.PASSWORD),
                             UserFieldsName.NICKNAME),

                Arguments.of(new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                     "ivan_not_well_formed", WellFormedUserData.EMAIL,
                                                     WellFormedUserData.PASSWORD),
                             UserFieldsName.NICKNAME)
        );
    }

    @Test
    void activate_changesUserStatusToActivated() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(BASE_PATH + "1/activate").contentType(MediaType.APPLICATION_JSON)
                                                                           .header(HEADER_X_AUTH_TOKEN, authToken))
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
        mockMvc.perform(put(BASE_PATH + "abc/activate").contentType(MediaType.APPLICATION_JSON)
                                                       .header(HEADER_X_AUTH_TOKEN, authToken))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void activate_returnsNotFoundResponse_WhenUserDoesntExist() throws Exception {
        mockMvc.perform(put(BASE_PATH + "5/activate").contentType(MediaType.APPLICATION_JSON)
                                                     .header(HEADER_X_AUTH_TOKEN, authToken))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andReturn();
    }

    @Test
    void ban_changeUserStatusToBanned() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(BASE_PATH + "1/ban").contentType(MediaType.APPLICATION_JSON)
                                                                      .header(HEADER_X_AUTH_TOKEN, authToken))
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
        mockMvc.perform(put(BASE_PATH + "abc/ban").contentType(MediaType.APPLICATION_JSON)
                                                  .header(HEADER_X_AUTH_TOKEN, authToken))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void ban_returnsNotFoundResponse_WhenUserDoesntExist() throws Exception {
        mockMvc.perform(put(BASE_PATH + "5/ban").contentType(MediaType.APPLICATION_JSON)
                                                .header(HEADER_X_AUTH_TOKEN, authToken))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andReturn();
    }

    @Test
    void login_returnsBadRequest_WhenRequestContainsBasicAuthenticationOnGetHttpMethod() throws Exception {
        mockMvc.perform(get(LOGIN_BASE_PATH).with(httpBasic(USERNAME, PASSWORD)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath(MESSAGE_JSON_PATH, equalTo(BASIC_AUTHENTICATION_NOT_ALLOWED_MESSAGE)))
               .andReturn();
    }

    @Test
    void login_returnsUnauthorized_WhenUserIsBanned() throws Exception {
        mockMvc.perform(post(LOGIN_BASE_PATH).with(httpBasic(USERNAME_BANNED, PASSWORD_BANNED)))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void login_returnsUnauthorized_WhenUserIsNotActivated() throws Exception {
        mockMvc.perform(post(LOGIN_BASE_PATH).with(httpBasic(USERNAME_NOT_ACTIVATED, PASSWORD_NOT_ACTIVATED)))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_invalidatesUserSession_WhenSessionActive() throws Exception {
        mockMvc.perform(post(BASE_PATH + "logout").header(HEADER_X_AUTH_TOKEN, authToken))
               .andDo(print())
               .andExpect(status().isOk());

        mockMvc.perform(get(BASE_PATH + "1").header(HEADER_X_AUTH_TOKEN, authToken))
               .andDo(print())
               .andExpect(status().isUnauthorized());
    }
}