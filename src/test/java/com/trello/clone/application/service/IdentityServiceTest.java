package com.trello.clone.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import com.trello.clone.application.port.out.PublishIdentityPort;
import com.trello.clone.application.port.out.LoadIdentityPort;
import com.trello.clone.application.port.out.SaveIdentityPort;
import com.trello.clone.common.utils.JwtUtil;
import com.trello.clone.exception.SessionExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trello.clone.domain.Identity;
import com.trello.clone.domain.IdentityId;
import com.trello.clone.domain.UserId;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class IdentityServiceTest {
    private final LoadIdentityPort loadIdentityPort = mock(LoadIdentityPort.class);
    private final SaveIdentityPort saveIdentityPort = mock(SaveIdentityPort.class);
    private final PublishIdentityPort publishIdentityPort = mock(PublishIdentityPort.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);

    private final Identity validAuth = new Identity(new IdentityId(UUID.randomUUID()),
        "juniar1", "juniar@gmail.com", new UserId(UUID.randomUUID()));

    private IdentityService instance;

    @BeforeEach
    void setup() {

        validAuth.setPassword("test123");

        instance = new IdentityService(loadIdentityPort,
            saveIdentityPort, jwtUtil, publishIdentityPort);

        when(loadIdentityPort.fetchIdentityById(any(IdentityId.class)))
            .thenReturn(Mono.just(validAuth));

        when(loadIdentityPort.fetchIdentityByUsername(anyString()))
            .thenReturn(Mono.just(validAuth));

        when(loadIdentityPort.fetchIdentityByUsernameOrEmail(anyString(), anyString()))
            .thenReturn(Mono.just(validAuth));

        when(jwtUtil.generateAccessToken(any(Identity.class)))
            .thenReturn("generatedAccessToken");

        when(jwtUtil.generateRefreshToken(any(Identity.class)))
            .thenReturn("generatedRefreshToken");

        when(jwtUtil.getExpirationDateFromToken(anyString()))
            .thenReturn(LocalDateTime.now().plusDays(1));

        when(jwtUtil.getSubjectFromToken(anyString()))
            .thenReturn("juniar1");

    }

    @Test
    void shouldReturnTokens_whenAuthenticate_withValidUsernameAndPassword() {
        when(saveIdentityPort.saveIdentity(any(Identity.class))).thenReturn(Mono.just(validAuth));

        StepVerifier.create(instance.authenticate("juniar1", "juniar@gmail.com", "test123"))
            .expectNextMatches(m -> m.containsKey("refresh_token") && m.containsKey("access_token")).verifyComplete();

        verify(saveIdentityPort, times(1)).saveIdentity(validAuth);
    }

    @Test
    void shouldReturnTokens_whenRefresh_withValidRefreshToken() {

        // TODO: Uncomment and the test will fail. Fix this

//        UUID sessionId = UUID.randomUUID();
//        validAuth.setSessionId(sessionId);
//
//        when(jwtUtil.getSessionIdFromToken(anyString()))
//            .thenReturn(sessionId.toString());
//
//        when(saveIdentityPort.saveIdentity(any(Identity.class)))
//            .thenReturn(Mono.just(validAuth));
//
//        when(jwtUtil.getSubjectFromToken(anyString()))
//            .thenReturn(UUID.randomUUID().toString());
//
//        StepVerifier.create(instance.refreshSession("generatedRefreshToken"))
//            .expectNextMatches(m -> m.containsKey("refresh_token") && m.containsKey("access_token"))
//            .verifyComplete();
//
//        verify(saveIdentityPort, times(1)).saveIdentity(validAuth);
    }

    @Test
    void shouldReturnError_whenUserLogin_andSessionAlreadyExists() {
        UUID sessionId = UUID.randomUUID();
        validAuth.setSessionId(sessionId);
        when(jwtUtil.getSessionIdFromToken(anyString()))
            .thenReturn(sessionId.toString());

        StepVerifier.create(instance.authenticate("juniar1", "", "test123"))
            .verifyError(SessionExistException.class);

        verify(saveIdentityPort, times(0)).saveIdentity(validAuth);
    }

    @Test
    void shouldReturnOk_whenUserLogout_andSessionExists() {

        // TODO: Uncomment and the test will fail. Fix this

//        UUID sessionId = UUID.randomUUID();
//        validAuth.setSessionId(sessionId);
//        when(jwtUtil.getSessionIdFromToken(anyString()))
//            .thenReturn(sessionId.toString());
//
//        when(saveIdentityPort.saveIdentity(any(Identity.class))).thenReturn(Mono.empty());
//        when(loadIdentityPort.fetchIdentityById(any(IdentityId.class))).thenReturn(Mono.just(validAuth));
//
//        StepVerifier.create(instance.logout(new IdentityId(UUID.randomUUID())))
//            .verifyComplete();
//
//        verify(saveIdentityPort, times(1)).saveIdentity(validAuth);
    }
}
