package com.trello.clone.adapter.out.publisher;

import com.trello.clone.application.port.out.PublishIdentityPort;
import com.trello.clone.domain.Identity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class IdentityPublisher implements PublishIdentityPort {
    @Override
    public Mono<Void> publishCreatedIdentity(Identity identity) {
        // TODO: Implement this
        log.error("Successfully register identity {}", identity.getId().id());
        return Mono.empty();
    }
}
