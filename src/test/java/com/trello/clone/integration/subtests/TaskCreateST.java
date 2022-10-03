package com.trello.clone.integration.subtests;

import com.trello.clone.adapter.in.rest.payload.TaskPayload;
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

public class TaskCreateST {
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
    @DisplayName("WHEN creating new task with valid payload THEN expect ok status")
    @Order(1)
    void whenCreatingNewTaskWithValidParams_thenExpectOkStatus() {
        testClient.post()
            .uri("/api/task")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userAuthMap.get("juniar1").accessToken())
            .body(Mono.just(new TaskPayload("foo", "bar", "baz")),
                TaskPayload.class)
            .exchange()
            .expectStatus().isOk();
    }

}
