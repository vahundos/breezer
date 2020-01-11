package com.github.vahundos.breezer.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(long id) {
        super("Entity with id = " + id + " not found");
    }
}
