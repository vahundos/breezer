package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;

public interface UserService {

    User register(UserRegistrationDto userDto);
}
