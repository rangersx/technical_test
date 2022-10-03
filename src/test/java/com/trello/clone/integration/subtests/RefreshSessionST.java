package com.trello.clone.integration.subtests;

import com.trello.clone.adapter.in.rest.result.AuthenticationResult;
import com.trello.clone.exception.SessionNotFoundException;
import com.trello.clone.exception.TokenInvalidatedException;
import com.trello.clone.integration.AlmendeServiceIT;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.trello.clone.integration.AlmendeServiceIT.jwtUtil;
import static com.trello.clone.integration.AlmendeServiceIT.userAuthMap;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RefreshSessionST {

    static WebTestClient testClient;
    static AuthenticationResult oldAuthResult;

    @BeforeAll
    static void init() {
        testClient = WebTestClient
            .bindToServer()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .baseUrl(AlmendeServiceIT.baseURL)
            .build();
    }

    @Test
    @DisplayName("WHEN refreshing with valid token THEN expect new token pair")
    @Order(1)
    void whenRefreshingWithValidToken_thenExpectNewTokenPair() {

        oldAuthResult = userAuthMap.get("juniar1");

        EntityExchangeResult<AuthenticationResult> result = testClient.get()
            .uri("/api/auth/refresh?refreshToken=" + oldAuthResult.refreshToken())
            .exchange()
            .expectStatus().isOk()
            .expectBody(AuthenticationResult.class)
            .returnResult();

        AuthenticationResult authenticationResult = result.getResponseBody();

        assertThat(authenticationResult).isNotNull();

        String oldSessionId = jwtUtil.getSessionIdFromToken(oldAuthResult.refreshToken());
        String newSessionId = jwtUtil.getSessionIdFromToken(authenticationResult.refreshToken());

        assertThat(authenticationResult.accessToken()).isNotEqualToIgnoringCase(oldAuthResult.accessToken());
        assertThat(authenticationResult.refreshToken()).isNotEqualToIgnoringCase(oldAuthResult.refreshToken());
        assertThat(newSessionId).isNotEqualToIgnoringCase(oldSessionId);

        userAuthMap.put("juniar1", authenticationResult);
    }

    @Test
    @DisplayName("WHEN refreshing with invalid session id THEN expect TokenInvalidatedException")
    @Order(2)
    void whenRefreshingWithInvalidSession_thenExpectInvalidatedError() {

        testClient.get()
            .uri("/api/auth/refresh?refreshToken=" + oldAuthResult.refreshToken())
            .exchange()
            .expectBody()
            .jsonPath("$.status").isEqualTo(TokenInvalidatedException.STATUS.value())
            .jsonPath("$.message").isEqualTo(TokenInvalidatedException.MESSAGE);
    }

    @Test
    @DisplayName("WHEN refreshing with old refresh token THEN expect SessionNotFoundException")
    @Order(3)
    void whenRefreshAgain_thenExpectSessionNotFoundError() {

        String refreshToken = userAuthMap.get("juniar1").refreshToken();

        testClient.get()
            .uri("/api/authenticate/v1/refresh?refreshToken=" + refreshToken)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody()
            .jsonPath("$.message").isEqualTo(SessionNotFoundException.MESSAGE);
    }
}
