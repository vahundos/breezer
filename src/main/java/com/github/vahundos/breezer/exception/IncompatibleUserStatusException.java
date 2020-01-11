package com.github.vahundos.breezer.exception;

import com.github.vahundos.breezer.model.UserStatus;

public class IncompatibleUserStatusException extends RuntimeException {

    public IncompatibleUserStatusException(UserStatus newStatus) {
        super("Can't change status to " + newStatus);
    }
}
