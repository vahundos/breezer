package com.github.vahundos.breezer.dto;

import com.github.vahundos.breezer.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserWithAuthTokenDto {

    private String authToken;

    private User user;
}
