package com.github.vahundos.breezer.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.vahundos.breezer.dto.UserLoginDto;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.service.UserService;
import com.github.vahundos.breezer.view.UserViews;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {

    private final UserService service;

    @GetMapping("/{id}")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public User get(@PathVariable long id) {
        return service.get(id);
    }

    @PostMapping("/login")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public User login(@RequestBody @Valid UserLoginDto user) {
        return service.login(user);
    }

    @PostMapping("/register")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public ResponseEntity<User> register(@RequestBody @Valid UserRegistrationDto user) {
        return new ResponseEntity<>(service.register(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/activate")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public User activate(@PathVariable long id) {
        return service.updateStatus(id, UserStatus.ACTIVATED);
    }

    @PutMapping("/{id}/ban")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public User ban(@PathVariable long id) {
        return service.updateStatus(id, UserStatus.BANNED);
    }
}
