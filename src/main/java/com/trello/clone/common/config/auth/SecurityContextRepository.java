package com.trello.clone.common.config.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final AuthenticationManager authenticationManager;
    private final String BEARER_STRING = "Bearer ";

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        throw new UnsupportedOperationException("Not yet supported.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
            .filter(authHeader -> authHeader.startsWith(BEARER_STRING))
            .flatMap(authHeader -> {
                String authToken = authHeader.substring(7);
                Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
                return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
            });
    }
}
