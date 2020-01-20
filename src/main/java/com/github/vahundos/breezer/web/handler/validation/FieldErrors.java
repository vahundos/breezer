package com.github.vahundos.breezer.web.handler.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class FieldErrors {

    private final String fieldName;

    private final List<String> errors = new ArrayList<>();
}
