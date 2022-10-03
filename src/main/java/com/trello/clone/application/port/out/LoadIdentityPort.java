package com.trello.clone.application.port.out;

import com.trello.clone.domain.Identity;
import com.trello.clone.domain.IdentityId;

import reactor.core.publisher.Mono;

public interface LoadIdentityPort {
    Mono<Identity> fetchIdentityById(IdentityId id);
    Mono<Identity> fetchIdentityByUsername(String username);
    Mono<Identity> fetchIdentityByEmail(String email);
    Mono<Identity> fetchIdentityByUsernameOrEmail(String username, String email);
}
