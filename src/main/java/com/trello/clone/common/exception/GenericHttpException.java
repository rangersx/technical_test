package com.trello.clone.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

public class GenericHttpException extends RuntimeException {


    @Getter
    private final HttpStatus status;

    /**
     * @param message error message
     * @param status  http status to use when returning {@link ResponseEntity}
     */
    public GenericHttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
