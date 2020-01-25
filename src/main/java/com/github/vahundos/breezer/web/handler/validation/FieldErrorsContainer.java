package com.github.vahundos.breezer.web.handler.validation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class FieldErrorsContainer {

    @Getter
    private final List<FieldErrors> container = new ArrayList<>();

    public void add(String fieldName, String errorMessage) {
        container.stream()
                 .filter(x -> x.getFieldName().equals(fieldName))
                 .findAny()
                 .ifPresentOrElse(x -> x.getErrors().add(errorMessage),
                                  () -> {
                                      FieldErrors fieldErrors = new FieldErrors(fieldName);
                                      fieldErrors.getErrors().add(errorMessage);
                                      container.add(fieldErrors);
                                  }
                 );
    }
}
