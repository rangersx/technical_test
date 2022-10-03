package com.trello.clone.application.port.out;

import com.trello.clone.domain.Identity;

import reactor.core.publisher.Mono;

public interface PublishIdentityPort {
    Mono<Void> publishCreatedIdentity(Identity identity);
}
