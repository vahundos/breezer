package com.github.vahundos.breezer.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.service.UserService;
import com.github.vahundos.breezer.view.UserViews;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = APPLICATION_JSON_VALUE)
public class UserRestController {

    public static final String AUTH_TOKEN = "authToken";

    private final UserService service;

    @PostMapping("/login")
    public Map<String, String> login(HttpSession httpSession) {
        return Map.of(AUTH_TOKEN, httpSession.getId());
    }

    @PostMapping("/logout")
    public void logout(HttpSession httpSession) {
        httpSession.invalidate();
    }

    @PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE)
    @JsonView(UserViews.WithoutSensitiveData.class)
    public ResponseEntity<User> register(@RequestBody @Valid UserRegistrationDto user) {
        return new ResponseEntity<>(service.register(user), HttpStatus.CREATED);
    }
}
