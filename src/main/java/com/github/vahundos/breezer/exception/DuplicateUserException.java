package com.github.vahundos.breezer.exception;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String duplicateItem, String duplicateItemValue) {
        super("User with " + duplicateItem + "=" + duplicateItemValue + " already exists");
    }
}
