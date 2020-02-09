package com.github.vahundos.breezer;

import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserRole;
import com.github.vahundos.breezer.model.UserStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static final String USERNAME = "ivanivanov";
    public static final String PASSWORD = "password1";

    public static User getUser1() {
        return new User(1L, "Ivan", "Ivanov", USERNAME, "ivan.ivanov@mail.net", UserStatus.ACTIVATED, PASSWORD,
                        Set.of(UserRole.USER, UserRole.ADMIN));
    }

    public static UserRegistrationDto getUserForRegistration() {
        return new UserRegistrationDto("Semen", "Semevnov", "senmv213", "semsnov1@mail.net", "password3");
    }
}
