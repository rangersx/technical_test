package com.trello.clone.integration.subtests;

import com.trello.clone.adapter.in.rest.payload.NewBoardPayload;
import com.trello.clone.integration.AlmendeServiceIT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.trello.clone.integration.AlmendeServiceIT.userAuthMap;

public class BoardCreationST {

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

    // TODO: add negative path tests here.
    //  i.e. tests for invalid payload or anything that should return an error

    @Test
    @DisplayName("WHEN creating new board with valid payload THEN expect ok status")
    @Order(2)
    void whenCreatingNewBoardWithValidPayload_thenExpectOkStatus() {
        testClient.post()
            .uri("/api/board")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userAuthMap.get("juniar1").accessToken())
            .body(Mono.just(new NewBoardPayload("foo")),
                NewBoardPayload.class)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    @DisplayName("WHEN fetching existing board THEN expect correct result")
    @Order(4)
    void whenFetchingExistingBoard_thenExpectCorrectResult() {
        testClient.get()
            .uri("/api/board?name=foo")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userAuthMap.get("juniar1").accessToken())
            .exchange()
            .expectBody();
    }
}
