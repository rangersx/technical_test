package com.trello.clone.application.port.in;

import java.util.Map;

import reactor.core.publisher.Mono;

public interface RefreshUseCase {
    Mono<Map<String, String>> refreshSession(String refreshToken);
}
