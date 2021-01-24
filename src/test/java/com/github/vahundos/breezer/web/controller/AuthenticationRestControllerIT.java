package com.github.vahundos.breezer.web.controller;

import com.github.vahundos.breezer.TestConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.vahundos.breezer.TestData.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthenticationRestControllerIT extends AbstractControllerIT {

    private static final String BASIC_AUTHENTICATION_NOT_ALLOWED_MESSAGE = "Basic authentication is supported " +
            "only for POST /authentication";

    private static final String MESSAGE_JSON_PATH = "$.message";

    @Test
    void login_requestAuthToken_WhenUserIsValid() {
        String authToken = getAuthToken();
        Assertions.assertThat(authToken).isNotEmpty();
    }

    @Test
    void login_returnsBadRequest_WhenRequestContainsBasicAuthenticationOnGetHttpMethod() throws Exception {
        mockMvc.perform(get(TestConstants.AUTHENTICATE_PATH).with(httpBasic(USERNAME, PASSWORD)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath(MESSAGE_JSON_PATH, equalTo(BASIC_AUTHENTICATION_NOT_ALLOWED_MESSAGE)))
               .andReturn();
    }

    @Test
    void login_returnsUnauthorized_WhenUserIsNotActivated() throws Exception {
        mockMvc.perform(post(TestConstants.AUTHENTICATE_PATH).with(httpBasic(USERNAME_NOT_ACTIVATED, PASSWORD_NOT_ACTIVATED)))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void login_returnsUnauthorized_WhenUserIsBanned() throws Exception {
        mockMvc.perform(post(TestConstants.AUTHENTICATE_PATH).with(httpBasic(USERNAME_BANNED, PASSWORD_BANNED)))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_invalidatesUserSession_WhenSessionActive() throws Exception {
        mockMvc.perform(delete(TestConstants.AUTHENTICATE_PATH).header(TestConstants.HEADER_X_AUTH_TOKEN, getAuthToken()))
               .andDo(print())
               .andExpect(status().isOk());
    }
}
