package com.github.vahundos.breezer.web.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/authentication", produces = APPLICATION_JSON_VALUE)
public class AuthenticationRestController {

    public static final String AUTH_TOKEN = "authToken";

    @PostMapping
    public Map<String, String> login(HttpSession httpSession) {
        return Map.of(AUTH_TOKEN, httpSession.getId());
    }

    @DeleteMapping
    public void logout(HttpSession httpSession) {
        httpSession.invalidate();
    }
}
