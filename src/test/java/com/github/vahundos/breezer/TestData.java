package com.github.vahundos.breezer;

import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static User getUser1() {
        return new User(1L, "Ivan", "Ivanov", "ivan.ivanov", "ivan.ivanov@mail.net", UserStatus.REGISTERED, "password1");
    }

    public static UserRegistrationDto getUserForRegistration() {
        return new UserRegistrationDto("Semen", "Semevnov", "senmv213", "semsnov1@mail.net", "password3");
    }
}
