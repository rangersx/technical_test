package com.trello.clone.common.config.db;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMongoAuditing
@EnableReactiveMongoRepositories(value = {
    //TODO: add your persistence here
    "com.trello.clone.adapter.out.persistence.mongo",
})
public class MongoReactiveConfig {

    @Bean
    ReactiveAuditorAware<UUID> auditorAware() {
        return () -> ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .filter(authentication -> authentication != null && authentication.isAuthenticated())
            .map(authentication -> UUID.fromString(authentication.getName()))
            .switchIfEmpty(Mono.just(UUID.fromString("00000000-0000-0000-0000-000000000000")));
    }

}
