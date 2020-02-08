package com.github.vahundos.breezer;

import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getStatus() == UserStatus.ACTIVATED, true, true,
              user.getStatus() != UserStatus.BANNED, user.getRoles());
    }
}
