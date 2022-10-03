package com.trello.clone.application.port.out;

import com.trello.clone.domain.Identity;

import reactor.core.publisher.Mono;

public interface SaveIdentityPort {
    Mono<Identity> saveIdentity(Identity identity);
}
