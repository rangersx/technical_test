package com.trello.clone.adapter.in.rest;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.trello.clone.adapter.in.rest.payload.LoginPayload;
import com.trello.clone.adapter.in.rest.payload.RegisterPayload;
import com.trello.clone.adapter.in.rest.result.AuthenticationResult;
import com.trello.clone.application.port.in.AuthenticateUseCase;
import com.trello.clone.application.port.in.RegistrationUseCase;
import com.trello.clone.application.port.in.command.RegistrationCommand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.trello.clone.application.port.in.LogoutUseCase;
import com.trello.clone.application.port.in.RefreshUseCase;
import com.trello.clone.domain.IdentityId;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
@Tag(name = "Authentication API", description = "Endpoints for user authentication")
public class IdentityController {

    private final AuthenticateUseCase authenticateUseCase;
    private final RegistrationUseCase registrationUseCase;
    private final RefreshUseCase refreshUseCase;
    private final LogoutUseCase logoutUseCase;

    @PostMapping("/login")
    public Mono<AuthenticationResult> login(@Valid @RequestBody LoginPayload payload) {
        Assert.isTrue(!StringUtils.isAllBlank(payload.email(), payload.username()),
            "com.trello.clone.email.username.required");
        return authenticateUseCase.authenticate(payload.username(), payload.email(), payload.password())
            .flatMap(map -> Mono.just(new AuthenticationResult(
                map.get("access_token"),
                map.get("refresh_token"))));
    }

    @GetMapping("/refresh")
    public Mono<AuthenticationResult> refresh(@RequestParam @NotBlank String refreshToken) {
        return refreshUseCase.refreshSession(refreshToken)
            .flatMap(map -> Mono.just(new AuthenticationResult(
                map.get("access_token"),
                map.get("refresh_token"))));
    }

    @PostMapping("/register")
    public Mono<Void> register(@Valid @RequestBody RegisterPayload payload) {
        return registrationUseCase.register(new RegistrationCommand(
            payload.username(),
            payload.email(),
            payload.password(),
            payload.firstName(),
            payload.lastName()));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("isAuthenticated()")
    public Mono<Void> logout(Authentication authentication) {
        log.debug("logout request from : {}", authentication.getName());
        return logoutUseCase.logout(new IdentityId(UUID.fromString(authentication.getName())));
    }
}
