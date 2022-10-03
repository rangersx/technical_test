package com.trello.clone.common.exception;

import org.springframework.http.HttpStatus;

public class PersistenceException extends GenericHttpException {
    public PersistenceException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
