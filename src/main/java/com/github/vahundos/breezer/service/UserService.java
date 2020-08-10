package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.model.User;

public interface UserService {

    User register(UserRegistrationDto userDto);
}
