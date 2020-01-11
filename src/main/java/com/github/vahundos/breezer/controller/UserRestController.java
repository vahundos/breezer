package com.github.vahundos.breezer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.service.UserService;
import com.github.vahundos.breezer.view.UserViews;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {

    private final UserService service;

    @GetMapping("/{id}")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public User get(@PathVariable(name = "id") long id) {
        return service.get(id);
    }

    @PutMapping("/register")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public User register(@RequestBody @Valid UserRegistrationDto user) {
        return service.register(user);
    }

    @PostMapping("/{id}/activate")
    public User activate(@PathVariable(name = "id") long id) {
        return service.updateStatus(id, UserStatus.ACTIVE);
    }

    @PostMapping("/{id}/ban")
    public User ban(@PathVariable(name = "id") long id) {
        return service.updateStatus(id, UserStatus.BANNED);
    }
}
