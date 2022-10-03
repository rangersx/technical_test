package com.trello.clone.integration.subtests;

import com.trello.clone.adapter.in.rest.payload.RegisterPayload;
import com.trello.clone.integration.AlmendeServiceIT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationST {

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
    @DisplayName("WHEN registering with invalid payload THEN expect error")
    @Order(1)
    void whenRegisterUsingInvalidPayload_ThenExpectError() {
        testClient.post()
            .uri("/api/auth/register")
            .body(Mono.just(new RegisterPayload(
                    "juniar1",
                    "juniar.almende.org",
                    "qwer",
                    "juniar",
                    "rakhman")),
                RegisterPayload.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("WHEN registering with valid payload THEN expect OK status")
    @Order(2)
    void whenRegisterUsingValidPayload_ThenExpectOKStatus() {
        testClient.post()
            .uri("/api/auth/register")
            .body(Mono.just(new RegisterPayload(
                    "juniar1",
                    "juniar@almende.org",
                    "qwerty123",
                    "juniar",
                    "rakhman")),
                RegisterPayload.class)
            .exchange()
            .expectStatus().isOk();
    }
}
