package com.github.vahundos.breezer.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.vahundos.breezer.AuthorizedUser;
import com.github.vahundos.breezer.dto.Message;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.dto.UserWithAuthTokenDto;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = APPLICATION_JSON_VALUE)
public class UserRestController {

    private final UserService userService;

    private final UserPictureService userPictureService;

    @GetMapping("/{id}")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public User get(@PathVariable long id) {
        return userService.get(id);
    }

    @PostMapping("/login")
    @JsonView(UserViews.WithoutSensitiveData.class)
    public ResponseEntity<UserWithAuthTokenDto> login(HttpSession httpSession, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        if (authorizedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.get(authorizedUser.getUserId());
        return ResponseEntity.ok(new UserWithAuthTokenDto(httpSession.getId(), user));
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
    public ResponseEntity<Message> saveUserPicture(@RequestParam("picture") MultipartFile picture, @PathVariable long userId) throws IOException {
        if (!"image/png".equals(picture.getContentType())) {
            return ResponseEntity.badRequest().body(new Message("only image png is acceptable"));
        }

        userPictureService.save(picture.getBytes(), userId);
        return ResponseEntity.ok().build();
    }
}
