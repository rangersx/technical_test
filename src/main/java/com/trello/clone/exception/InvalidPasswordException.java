package com.trello.clone.exception;

import org.springframework.http.HttpStatus;

import com.trello.clone.common.exception.GenericHttpException;

public class InvalidPasswordException extends GenericHttpException {

    public final static String MESSAGE = "com.trello.clone.identity.password.invalid";
    public final static HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public InvalidPasswordException() {
        super(MESSAGE, STATUS);
    }
}
