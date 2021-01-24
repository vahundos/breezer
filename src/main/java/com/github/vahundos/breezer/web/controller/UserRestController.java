package com.github.vahundos.breezer.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.service.UserService;
import com.github.vahundos.breezer.view.UserViews;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = APPLICATION_JSON_VALUE)
public class UserRestController {

    private final UserService service;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @JsonView(UserViews.WithoutSensitiveData.class)
    public ResponseEntity<User> register(@RequestBody @Valid UserRegistrationDto user) {
        return new ResponseEntity<>(service.register(user), HttpStatus.CREATED);
    }
}
