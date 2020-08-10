package com.github.vahundos.breezer.web.handler;

import com.github.vahundos.breezer.exception.DuplicateUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class MainExceptionHandler {

    private static final String ERROR_MESSAGE = "Exception occurred";

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMediaTypeNotSupportedException.class,
            MethodArgumentNotValidException.class, DuplicateUserException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(Exception e) {
        log.error(ERROR_MESSAGE, e);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.error(ERROR_MESSAGE, e);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
