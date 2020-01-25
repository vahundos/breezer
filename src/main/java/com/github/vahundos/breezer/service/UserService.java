package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.dto.UserLoginDto;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;

public interface UserService {

    User get(long id);

    User login(UserLoginDto userDto);

    User register(UserRegistrationDto userDto);

    User updateStatus(long userId, UserStatus newStatus);
}
