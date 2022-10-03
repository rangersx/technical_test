package com.trello.clone.common.advice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private final int status;
    private final String message;
    private final Date timestamp = new Date();
    private final String path;

    private String stackTrace;
    private List<ValidationError> validationErrors;

    public void addValidationError(String field, String message) {
        if (Objects.isNull(validationErrors)) {
            validationErrors = new ArrayList<>();
        }
        validationErrors.add(new ValidationError(field, message));
    }

    private record ValidationError(String field, String message) {
    }
}
