package com.github.vahundos.breezer.controller;

import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {

    private final UserService service;

    @GetMapping("/{id}")
    public User get(@PathVariable(name = "id") long id) {
        return service.get(id);
    }

    @PutMapping("/register")
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
