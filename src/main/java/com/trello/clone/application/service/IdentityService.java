package com.trello.clone.application.service;

import com.trello.clone.application.port.in.AuthenticateUseCase;
import com.trello.clone.application.port.in.RegistrationUseCase;
import com.trello.clone.common.utils.JwtUtil;
import com.trello.clone.exception.InvalidPasswordException;
import com.trello.clone.exception.SessionExistException;
import com.trello.clone.exception.UsernameAlreadyExistException;
import com.trello.clone.application.port.in.LogoutUseCase;
import com.trello.clone.application.port.in.RefreshUseCase;
import com.trello.clone.application.port.in.command.RegistrationCommand;
import com.trello.clone.application.port.out.LoadIdentityPort;
import com.trello.clone.application.port.out.PublishIdentityPort;
import com.trello.clone.application.port.out.SaveIdentityPort;
import com.trello.clone.domain.Identity;
import com.trello.clone.domain.IdentityId;
import com.trello.clone.exception.IdentityNotFoundException;
import com.trello.clone.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityService implements AuthenticateUseCase, RegistrationUseCase, RefreshUseCase, LogoutUseCase {

    private final LoadIdentityPort loadIdentityPort;
    private final SaveIdentityPort saveIdentityPort;

    private final JwtUtil jwtUtil;

    private final PublishIdentityPort publishIdentityPort;

    private final String ACCESS_TOKEN_STR = "access_token";
    private final String REFRESH_TOKEN_STR = "refresh_token";

    @Override
    public Mono<Map<String, String>> authenticate(String username, String email, String password) {
        return loadIdentityPort.fetchIdentityByUsernameOrEmail(username, email)
            .switchIfEmpty(Mono.error(IdentityNotFoundException::new))
            .filter(identity -> identity.passwordMatches(password))
            .switchIfEmpty(Mono.error(InvalidPasswordException::new))
            .filter(Identity::isSessionNotExist)
            .switchIfEmpty(Mono.error(SessionExistException::new))
            .map(Identity::newSession)
            .flatMap(saveIdentityPort::saveIdentity)
            .map(identity -> {
                Map<String, String> tokens = new HashMap<>();
                tokens.put(ACCESS_TOKEN_STR, jwtUtil.generateAccessToken(identity));
                tokens.put(REFRESH_TOKEN_STR, jwtUtil.generateRefreshToken(identity));
                return tokens;
            });
    }

    @Override
    public Mono<Map<String, String>> refreshSession(String refreshToken) {
        //TODO: Implement this
        return Mono.empty();
    }

    @Override
    public Mono<Void> register(RegistrationCommand command) {
        return loadIdentityPort.fetchIdentityByUsername(command.username())
            .hasElement()
            .flatMap(hasElement -> hasElement ? Mono.error(UsernameAlreadyExistException::new) :
                Mono.just(new Identity(
                    null,
                    command.username(),
                    command.email(),
                    new UserId(UUID.randomUUID())
                )))
            .map(identity -> identity.setPassword(command.password()))
            .flatMap(saveIdentityPort::saveIdentity)
            .doOnNext(publishIdentityPort::publishCreatedIdentity)
            .then();
    }

    @Override
    public Mono<Void> logout(IdentityId id) {
        //TODO: Implement this
        return Mono.empty();
    }
}
