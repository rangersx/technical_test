package com.trello.clone.adapter.out.persistence.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface IdentityRepository extends ReactiveMongoRepository<IdentityEntity, UUID> {
    Mono<IdentityEntity> findByUsername(String username);
    Mono<IdentityEntity> findByEmailAddress(String emailAddress);
}
