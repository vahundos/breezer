package com.github.vahundos.breezer;

import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
public class AuthorizedUser extends org.springframework.security.core.userdetails.User {

    @Getter
    private final long userId;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getStatus() == UserStatus.ACTIVATED, true, true,
              user.getStatus() != UserStatus.BANNED, user.getRoles());
        this.userId = user.getId();
    }
}
