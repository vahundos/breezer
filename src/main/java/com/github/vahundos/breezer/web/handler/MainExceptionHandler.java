package com.github.vahundos.breezer.web.handler;

import com.github.vahundos.breezer.exception.DuplicateUserException;
import com.github.vahundos.breezer.exception.EntityNotFoundException;
import com.github.vahundos.breezer.exception.IncompatibleUserStatusException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class MainExceptionHandler {

    private static final String ERROR_MESSAGE = "Exception occurred";

    @ExceptionHandler({DuplicateUserException.class, IncompatibleUserStatusException.class,
                       MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ExceptionDetails> handleMethodArgumentNotValidException(Exception e) {
        log.error(ERROR_MESSAGE, e);
        return new ResponseEntity<>(new ExceptionDetails(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error(ERROR_MESSAGE, e);
        return new ResponseEntity<>(new ExceptionDetails(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        log.error(ERROR_MESSAGE, e);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    @AllArgsConstructor
    private static class ExceptionDetails {
        private final String message;
    }
}
