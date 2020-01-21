package com.github.vahundos.breezer.exception;

import lombok.Getter;

public class DuplicateUserException extends RuntimeException {

    @Getter
    private final String duplicateItem;

    public DuplicateUserException(String duplicateItem, String duplicateItemValue) {
        super("User with " + duplicateItem + " " + duplicateItemValue + " already exists");
        this.duplicateItem = duplicateItem;
    }
}
