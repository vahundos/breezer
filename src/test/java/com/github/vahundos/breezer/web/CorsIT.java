package com.github.vahundos.breezer.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CorsIT {

    private static final String REQUEST_PATH = "/users/login";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsOkResponse_WhenCorsHeadersAreValid() throws Exception {
        mockMvc.perform(options(REQUEST_PATH).headers(validCorsHeaders(null)))
               .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCorsHeaders")
    void returnsForbiddenResponse_WhenCrossOriginRequestBlocked(HttpHeaders httpHeaders) throws Exception {
        mockMvc.perform(options(REQUEST_PATH).headers(httpHeaders))
               .andExpect(status().isForbidden());
    }


    private static Stream<Arguments> provideInvalidCorsHeaders() {
        return Stream.of(
                Arguments.of(validCorsHeaders(h -> h.put(HttpHeaders.ORIGIN, of("http://localhost:7080")))),
                Arguments.of(validCorsHeaders(h -> h.put(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, of(HttpMethod.DELETE.name())))),
                Arguments.of(validCorsHeaders(h -> h.put(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, of("CustomHeader"))))
        );
    }

    private static HttpHeaders validCorsHeaders(Consumer<HttpHeaders> httpHeadersConsumer) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ORIGIN, "http://localhost:8000");
        httpHeaders.add(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.POST.name());
        httpHeaders.add(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, String.join(",", HttpHeaders.ACCEPT,
                                                                                HttpHeaders.CONTENT_TYPE,
                                                                                HttpHeaders.AUTHORIZATION,
                                                                                "X-Auth-Token"));

        if (httpHeadersConsumer != null) {
            httpHeadersConsumer.accept(httpHeaders);
        }

        return httpHeaders;
    }
}
