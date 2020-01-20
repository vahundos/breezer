package com.github.vahundos.breezer.web.handler;

import com.github.vahundos.breezer.exception.DuplicateUserException;
import com.github.vahundos.breezer.exception.EntityNotFoundException;
import com.github.vahundos.breezer.exception.IncompatibleUserStatusException;
import com.github.vahundos.breezer.web.handler.validation.FieldErrors;
import com.github.vahundos.breezer.web.handler.validation.FieldErrorsContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Slf4j
@ControllerAdvice
public class MainExceptionHandler {

    private static final String ERROR_MESSAGE = "Exception occurred";

    @ExceptionHandler({DuplicateUserException.class, IncompatibleUserStatusException.class,
                       MethodArgumentTypeMismatchException.class, HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<ExceptionDetails> handleMethodArgumentNotValidException(Exception e) {
        log.error(ERROR_MESSAGE, e);
        return new ResponseEntity<>(new ExceptionDetails(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldErrors>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldErrorsContainer holder = new FieldErrorsContainer();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> holder.add(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ResponseEntity<>(holder.getContainer(), HttpStatus.BAD_REQUEST);
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
