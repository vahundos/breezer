package com.github.vahundos.breezer.dto;

import com.github.vahundos.breezer.constant.ValidationConstants;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class UserLoginDto {

    @NotNull
    @Size(min = 2, max = 256)
    private String login;

    @NotNull
    @Size(min = ValidationConstants.PASSWORD_MIN_LENGTH, max = ValidationConstants.PASSWORD_MAX_LENGTH)
    private String password;
}
