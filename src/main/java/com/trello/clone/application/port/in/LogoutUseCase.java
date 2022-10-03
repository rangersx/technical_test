package com.trello.clone.application.port.in;

import com.trello.clone.domain.IdentityId;

import reactor.core.publisher.Mono;

public interface LogoutUseCase {
    Mono<Void> logout(IdentityId id);
}
