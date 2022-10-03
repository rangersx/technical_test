package com.trello.clone.exception;

import org.springframework.http.HttpStatus;

import com.trello.clone.common.exception.GenericHttpException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionNotFoundException extends GenericHttpException {

    public final static String MESSAGE = "com.trello.clone.identity.session.not.found";
    public final static HttpStatus STATUS = HttpStatus.CONFLICT;

    public SessionNotFoundException() {
        super(MESSAGE, STATUS);
    }
}
