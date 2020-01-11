package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;

public interface UserService {

    User get(long id);

    User register(User user);

    User updateStatus(long userId, UserStatus newStatus);
}
