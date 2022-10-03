package com.trello.clone.integration.subtests;

import static com.trello.clone.integration.AlmendeServiceIT.userAuthMap;
import static org.assertj.core.api.Assertions.assertThat;

import com.trello.clone.adapter.in.rest.payload.LoginPayload;
import com.trello.clone.integration.AlmendeServiceIT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.trello.clone.adapter.in.rest.result.AuthenticationResult;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationST {

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
    @DisplayName("WHEN logging in with correct username THEN expect access and refresh token")
    @Order(1)
    void whenLoginWithValidUsername_ThenExpectTokenPair() {
        EntityExchangeResult<AuthenticationResult> result = testClient.post()
            .uri("/api/auth/login")
            .body(Mono.just(new LoginPayload(
                    "juniar1", "", "qwerty123")),
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

    @Test
    @DisplayName("WHEN logging in with correct email AND session exists THEN expect session already exists error")
    @Order(3)
    void whenLoginWithValidEmail_AndSessionExists_ThenExpectConflictError() {
        testClient.post()
            .uri("/api/auth/login")
            .body(Mono.just(new LoginPayload(
                    "", "juniar@almende.org", "qwerty123")),
                LoginPayload.class)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }
}
