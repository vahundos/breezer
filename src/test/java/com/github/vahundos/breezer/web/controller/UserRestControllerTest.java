package com.github.vahundos.breezer.web.controller;

import com.github.vahundos.breezer.web.handler.MainExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRestController userRestController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userRestController)
                                      .setControllerAdvice(new MainExceptionHandler())
                                      .build();
    }

    @Test
    void get_returnsInternalServerError_WhenUnexpectedExceptionOccurs() throws Exception {
        var id = 1L;
        when(userRestController.get(id)).thenThrow(new RuntimeException("Unexpected exception"));

        mockMvc.perform(get("/users/" + id).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isInternalServerError());
    }
}
