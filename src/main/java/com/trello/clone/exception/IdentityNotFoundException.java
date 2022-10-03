package com.trello.clone.exception;

import com.trello.clone.common.exception.GenericHttpException;
import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdentityNotFoundException extends GenericHttpException {

    public final static String MESSAGE = "com.trello.clone.identity.notFound";
    public final static HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public IdentityNotFoundException() {
        super(MESSAGE, STATUS);
    }
}
