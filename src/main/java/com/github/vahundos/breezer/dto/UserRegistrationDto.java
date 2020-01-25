package com.github.vahundos.breezer.dto;

import com.github.vahundos.breezer.constant.ValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotNull
    @Size(min = ValidationConstants.NAME_MIN_LENGTH, max = ValidationConstants.NAME_MAX_LENGTH)
    private String firstName;

    @NotNull
    @Size(min = ValidationConstants.NAME_MIN_LENGTH, max = ValidationConstants.NAME_MAX_LENGTH)
    private String secondName;

    @NotNull
    @Size(min = 2, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "nickname should contain only letters and numbers")
    private String nickname;

    @Email
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @Size(min = ValidationConstants.PASSWORD_MIN_LENGTH, max = ValidationConstants.PASSWORD_MAX_LENGTH)
    private String password;
}
