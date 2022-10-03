package com.trello.clone.integration.subtests;

import com.trello.clone.integration.AlmendeServiceIT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.trello.clone.integration.AlmendeServiceIT.userAuthMap;

public class TaskDeleteST {
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
    @DisplayName("WHEN deleting task with valid params THEN expect ok status")
    @Order(1)
    void whenDeletingTaskWithValidParams_thenExpectOkStatus() {
        testClient.delete()
            .uri("/api/task?board=foo&column=bar&task=baz")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userAuthMap.get("juniar1").accessToken())
            .exchange()
            .expectStatus().isOk();
    }
}
