package com.github.vahundos.breezer.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vahundos.breezer.TestData;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.UserRole;
import com.github.vahundos.breezer.web.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static com.github.vahundos.breezer.TestData.*;
import static com.github.vahundos.breezer.web.controller.UserRestController.AUTH_TOKEN;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@Sql(scripts = "/init-data.sql")
class UserRestControllerIT {

    private static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";

    private static final String BASIC_AUTHENTICATION_NOT_ALLOWED_MESSAGE = "Basic authentication is supported " +
            "only for POST /users/login";

    private static final String BASE_PATH = "/users";
    private static final String LOGIN_BASE_PATH = BASE_PATH + "/login";
    private static final String LOGOUT_BASE_PATH = BASE_PATH + "/logout";
    private static final String REGISTER_BASE_PATH = BASE_PATH + "/register";

    private static final String MESSAGE_JSON_PATH = "$.message";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

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
    void register_createsAndReturnsRegisteredUserWithId() throws Exception {
        mockMvc.perform(post(REGISTER_BASE_PATH)
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
    @MethodSource("com.github.vahundos.breezer.NotWellFormedUserRegistrationDtoData#provideNotWellFormedUserFields")
    void register_returnsBadRequestResponse_WhenRequestBodyIsNotWellFormed(UserRegistrationDto notWellFormedUser, String fieldName) throws Exception {
        mockMvc.perform(post(REGISTER_BASE_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HEADER_X_AUTH_TOKEN, authToken)
                                .content(objectMapper.writeValueAsString(notWellFormedUser)))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$[0].fieldName", equalTo(fieldName)));
    }

    @Test
    void register_returnsBadRequestResponse_WhenMediaTypeIsNotSupported() throws Exception {
        mockMvc.perform(post(REGISTER_BASE_PATH)
                                .contentType(MediaType.APPLICATION_XML_VALUE)
                                .header(HEADER_X_AUTH_TOKEN, authToken)
                                .content("{}"))
               .andDo(print())
               .andExpect(status().isBadRequest());
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
        mockMvc.perform(post(LOGOUT_BASE_PATH).header(HEADER_X_AUTH_TOKEN, authToken))
               .andDo(print())
               .andExpect(status().isOk());
    }
}