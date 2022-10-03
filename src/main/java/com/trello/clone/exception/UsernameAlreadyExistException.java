package com.trello.clone.exception;

import org.springframework.http.HttpStatus;

import com.trello.clone.common.exception.GenericHttpException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsernameAlreadyExistException extends GenericHttpException {

    public final static String MESSAGE = "com.trello.clone.identity.username.alreadyExists";
    public final static HttpStatus STATUS = HttpStatus.CONFLICT;

    public UsernameAlreadyExistException() {
        super(MESSAGE, STATUS);
    }
}
