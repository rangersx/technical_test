package com.trello.clone.application.port.in;

import com.trello.clone.application.port.in.command.RegistrationCommand;

import reactor.core.publisher.Mono;

public interface RegistrationUseCase {
    Mono<Void> register(RegistrationCommand command);
}
