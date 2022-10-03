package com.trello.clone.common.advice;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.trello.clone.common.exception.GenericHttpException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.auth0.jwt.exceptions.TokenExpiredException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class TaskAgileControllerAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Mono<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException exception, ServerHttpRequest request) {
        return Mono.just(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", ")),
                request.getPath().value()))
            .map(errorResponse -> {
                exception.getFieldErrors().forEach(fieldError ->
                    errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage()));
                return errorResponse;
            });
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Mono<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception, ServerHttpRequest request) {
        return Mono.just(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")),
                request.getPath().value()))
            .map(errorResponse -> {
                exception.getConstraintViolations().forEach(constraintViolation ->
                    errorResponse.addValidationError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));
                return errorResponse;
            });
    }

    @ExceptionHandler(GenericHttpException.class)
    Mono<ResponseEntity<ErrorResponse>> handleGenericHttpException(GenericHttpException exception, ServerHttpRequest request) {
        log.error("Generic HTTP exception : {} - {}", exception.getStatus(), exception.getMessage());
        return Mono.just(ResponseEntity.status(exception.getStatus())
            .body(new ErrorResponse(exception.getStatus().value(), exception.getMessage(), request.getPath().value()))
        );
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Mono<ErrorResponse> handleTokenExpiredException(TokenExpiredException exception, ServerHttpRequest request) {
        return Mono.just(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), request.getPath().value()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Mono<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception, ServerHttpRequest request) {
        return Mono.just(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getPath().value()));
    }

}
