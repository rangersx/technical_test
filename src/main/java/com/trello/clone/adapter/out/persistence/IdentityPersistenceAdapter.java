package com.trello.clone.adapter.out.persistence;

import com.trello.clone.application.port.out.LoadIdentityPort;
import com.trello.clone.application.port.out.SaveIdentityPort;
import com.trello.clone.common.exception.PersistenceException;
import com.trello.clone.adapter.out.persistence.mongo.IdentityEntity;
import com.trello.clone.adapter.out.persistence.mongo.IdentityRepository;
import com.trello.clone.domain.Identity;
import com.trello.clone.domain.IdentityId;
import com.trello.clone.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IdentityPersistenceAdapter implements LoadIdentityPort, SaveIdentityPort {

    private final IdentityRepository identityRepository;

    @Override
    public Mono<Identity> fetchIdentityByUsernameOrEmail(String username, String email) {
        return StringUtils.isNotBlank(username) ?
            fetchIdentityByUsername(username) :
            fetchIdentityByEmail(email);
    }

    @Override
    public Mono<Identity> fetchIdentityById(IdentityId identityId) {
        log.debug("Fetching identity with id {}", identityId.id());
        return identityRepository.findById(identityId.id())
            .map(this::entityToDomainMapper);
    }

    @Override
    public Mono<Identity> fetchIdentityByUsername(String username) {
        log.debug("Fetching identity with username {}", username);
        return identityRepository.findByUsername(username)
            .map(this::entityToDomainMapper);
    }

    @Override
    public Mono<Identity> fetchIdentityByEmail(String email) {
        log.debug("Fetching identity with email {}", email);
        return identityRepository.findByEmailAddress(email)
            .map(this::entityToDomainMapper);
    }

    @Override
    public Mono<Identity> saveIdentity(Identity domain) {
        if (domain.getId() == null) {
            log.debug("Saving new identity : {}", domain);
            return identityRepository.save(domainToEntityMapper(domain))
                .onErrorMap(ex -> new PersistenceException(ex.getMessage()))
                .map(this::entityToDomainMapper);
        } else {
            return identityRepository.findById(domain.getId().id())
                .map(entity -> domainToExistingEntityMapper(domain, entity))
                .flatMap(identityRepository::save)
                .doOnNext(entity -> log.debug("Updated identity : {}", entity))
                .map(this::entityToDomainMapper);
        }
    }

    private IdentityEntity domainToEntityMapper(Identity identity) {
        log.debug("Mapping domain : {}", identity);
        return new IdentityEntity(
            identity.getId() == null ? UUID.randomUUID() : identity.getId().id(),
            identity.getUsername(),
            identity.getEmailAddress(),
            identity.getPassword(),
            identity.getSessionId() == null ? null : identity.getSessionId(),
            identity.getUser().id());
    }

    private Identity entityToDomainMapper(IdentityEntity entity) {
        log.debug("Mapping entity : {}", entity);
        return new Identity(
            new IdentityId(entity.getId()),
            entity.getUsername(),
            entity.getEmailAddress(),
            new UserId(entity.getUserId()))
            .setPassword(entity.getPassword())
            .setSessionId(entity.getSessionId());
    }

    private IdentityEntity domainToExistingEntityMapper(Identity domain, IdentityEntity entity) {
        BeanUtils.copyProperties(domain, entity);
        entity.setId(domain.getId().id());
        entity.setUserId(domain.getUser().id());
        log.debug("Updated entity : {}", entity);
        return entity;
    }
}
