package com.trello.clone.integration.subtests;

import com.trello.clone.adapter.in.rest.payload.LoginPayload;
import com.trello.clone.adapter.in.rest.result.AuthenticationResult;
import com.trello.clone.integration.AlmendeServiceIT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;
import reactor.core.publisher.Mono;

import static com.trello.clone.integration.AlmendeServiceIT.userAuthMap;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExistingUserST {
    static WebTestClient testClient;

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
    @DisplayName("WHEN re-login with invalid password THEN expect invalid password error")
    @Order(1)
    void whenLoginWithInvalidPassword_thenExpectError() {
        testClient.post()
            .uri("/api/auth/login")
            .body(Mono.just(new LoginPayload("", "juniar@almende.org", "qwdsfa123")),
                LoginPayload.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("WHEN re-login with valid password THEN expect new token pair")
    @Order(2)
    void whenLoginWithValidUsernamePassword_thenExpectNewTokenPair() {
        EntityExchangeResult<AuthenticationResult> result = testClient.post()
            .uri("/api/auth/login")
            .body(Mono.just(new LoginPayload(
                    "", "juniar@almende.org", "qwerty123")),
                LoginPayload.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(AuthenticationResult.class)
            .returnResult();

        AuthenticationResult authenticationResult = result.getResponseBody();

        assertThat(authenticationResult).isNotNull();
        assertThat(authenticationResult.accessToken()).isNotBlank();
        assertThat(authenticationResult.refreshToken()).isNotBlank();

        userAuthMap.put("juniar1", authenticationResult);
    }
}
