package com.trello.clone.application.port.in;

import java.util.Map;

import reactor.core.publisher.Mono;

public interface AuthenticateUseCase {
    Mono<Map<String, String>> authenticate(String username, String email, String password);
}
