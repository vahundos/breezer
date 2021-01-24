package com.github.vahundos.breezer.web.controller;

import com.github.vahundos.breezer.TestConstants;
import com.github.vahundos.breezer.TestData;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserRestControllerIT extends AbstractControllerIT {

    private static final String USERS_PATH = "/users";

    @Test
    void register_createsAndReturnsRegisteredUserWithId() throws Exception {
        mockMvc.perform(post(USERS_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(TestConstants.HEADER_X_AUTH_TOKEN, getAuthToken())
                                .content(objectMapper.writeValueAsString(TestData.getUserForRegistration())))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id", equalTo(5)))
               .andExpect(jsonPath("$.roles[0]", equalTo(UserRole.USER.name())));
    }

    @ParameterizedTest
    @MethodSource("com.github.vahundos.breezer.NotWellFormedUserRegistrationDtoData#provideNotWellFormedUserFields")
    void register_returnsBadRequestResponse_WhenRequestBodyIsNotWellFormed(UserRegistrationDto notWellFormedUser) throws Exception {
        mockMvc.perform(post(USERS_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(TestConstants.HEADER_X_AUTH_TOKEN, getAuthToken())
                                .content(objectMapper.writeValueAsString(notWellFormedUser)))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }

    @Test
    void register_returnsBadRequestResponse_WhenMediaTypeIsNotSupported() throws Exception {
        mockMvc.perform(post(USERS_PATH)
                                .contentType(MediaType.APPLICATION_XML_VALUE)
                                .header(TestConstants.HEADER_X_AUTH_TOKEN, getAuthToken())
                                .content("{}"))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }
}
