package com.trello.clone.exception;

import org.springframework.http.HttpStatus;

import com.trello.clone.common.exception.GenericHttpException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenExpiredException extends GenericHttpException {

    public final static String MESSAGE = "com.trello.clone.identity.refresh.token.expired";
    public final static HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public TokenExpiredException() {
        super(MESSAGE, STATUS);
    }
}
