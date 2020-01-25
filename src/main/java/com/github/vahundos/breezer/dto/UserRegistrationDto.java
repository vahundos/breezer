package com.github.vahundos.breezer.dto;

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
    @Size(min = 3, max = 20)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 20)
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
    @Size(min = 6, max = 50)
    private String password;
}
