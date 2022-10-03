package com.trello.clone.common.config.auth;

import java.util.stream.Collectors;

import com.trello.clone.common.utils.JwtUtil;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    private final String ROLE_STR = "ROLE_";

    @Override
    public Mono<Authentication> authenticate(@NonNull Authentication authentication) {

        Assert.notNull(authentication.getCredentials(), "com.trello.clone.credentials.null");
        String authToken = authentication.getCredentials().toString();

        return Mono.just(new UsernamePasswordAuthenticationToken(
            jwtUtil.getSubjectFromToken(authToken),
            null,
            jwtUtil.getRolesFromToken(authToken)
                .stream()
                .map(s -> ROLE_STR + s)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())));
    }
}
