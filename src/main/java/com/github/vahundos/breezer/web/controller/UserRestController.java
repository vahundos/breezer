package com.github.vahundos.breezer.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.vahundos.breezer.AuthorizedUser;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserPicture;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.service.UserPictureService;
import com.github.vahundos.breezer.service.UserService;
import com.github.vahundos.breezer.view.UserViews;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = APPLICATION_JSON_VALUE)
public class UserRestController {

    public static final String AUTH_TOKEN = "authToken";

    private final UserService userService;

    private final UserPictureService userPictureService;

    @GetMapping("/{id}")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public User get(@PathVariable long id) {
        return userService.get(id);
    }

    @PostMapping("/login")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public Map<String, Object> login(HttpSession httpSession, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        User user = userService.get(authorizedUser.getUserId());
        return Map.of(AUTH_TOKEN, httpSession.getId(), "user", user);
    }

    @GetMapping("/authorizedId")
    public Map<String, Long> authorizedUserId(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        return Map.of("id", authorizedUser.getUserId());
    }

    @PostMapping("/logout")
    public void logout(HttpSession httpSession) {
        httpSession.invalidate();
    }

    @PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE)
    @JsonView(UserViews.WithoutSensitiveData.class)
    public ResponseEntity<User> register(@RequestBody @Valid UserRegistrationDto user) {
        return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/activate")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public User activate(@PathVariable long id) {
        return userService.updateStatus(id, UserStatus.ACTIVATED);
    }

    @PutMapping("/{id}/ban")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public User ban(@PathVariable long id) {
        return userService.updateStatus(id, UserStatus.BANNED);
    }

    @GetMapping(path = "/{userId}/picture", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getPictureByUserId(@PathVariable long userId) {
        UserPicture userPicture = userPictureService.getByUserId(userId);
        ByteArrayResource picture = new ByteArrayResource(userPicture.getPicture());

        return ResponseEntity.ok(picture);
    }

    @PostMapping("/{userId}/picture")
    public void saveUserPicture(@RequestParam("picture") MultipartFile picture, @PathVariable long userId) throws IOException {
        userPictureService.save(picture.getBytes(), userId);
    }
}
