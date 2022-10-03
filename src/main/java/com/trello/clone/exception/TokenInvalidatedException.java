package com.trello.clone.exception;

import com.trello.clone.common.exception.GenericHttpException;
import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenInvalidatedException extends GenericHttpException {

    public final static String MESSAGE = "com.trello.clone.identity.refresh.token.invalidated";
    public final static HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public TokenInvalidatedException() {
        super(MESSAGE, STATUS);
    }
}
