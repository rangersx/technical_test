package com.trello.clone.integration;

import com.trello.clone.common.utils.JwtUtil;
import com.trello.clone.adapter.in.rest.result.AuthenticationResult;
import com.trello.clone.integration.subtests.AuthenticationST;
import com.trello.clone.integration.subtests.BoardCreationST;
import com.trello.clone.integration.subtests.BoardDeleteST;
import com.trello.clone.integration.subtests.ColumnCreateST;
import com.trello.clone.integration.subtests.ColumnDeleteST;
import com.trello.clone.integration.subtests.ExistingUserST;
import com.trello.clone.integration.subtests.RefreshSessionST;
import com.trello.clone.integration.subtests.RegistrationST;
import com.trello.clone.integration.subtests.TaskCreateST;
import com.trello.clone.integration.subtests.TaskDeleteST;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.containers.DockerComposeContainer.RemoveImages;

@Slf4j
@Testcontainers
public class AlmendeServiceIT {

    private final static int SERVICE_PORT = 9091;
    private final static int MONGO_PORT = 27017;

    private final static String MONGO_SERVICE_NAME = "mongo_1";
    private final static String MAIN_SERVICE_NAME = "almende-service_1";
    public final static String BEARER_STRING = "Bearer ";

    public static String baseURL;

    public static WebTestClient testClient;

    public static Map<String, AuthenticationResult> userAuthMap;

    public static JwtUtil jwtUtil = new JwtUtil();

    @Container
    private static final DockerComposeContainer<?> compose =
        new DockerComposeContainer<>(new File("deploy/test-compose.yml"))
            .withRemoveImages(RemoveImages.ALL)
            .withExposedService(MONGO_SERVICE_NAME, MONGO_PORT, Wait.forListeningPort())
            .withExposedService(MAIN_SERVICE_NAME, SERVICE_PORT, Wait.forListeningPort())
            .withBuild(true)
            .withLocalCompose(true);

    private AlmendeServiceIT() {}


    @BeforeAll
    public static void init() {

        baseURL = "http://" + compose.getServiceHost(MAIN_SERVICE_NAME, SERVICE_PORT)
            + ":" + compose.getServicePort(MAIN_SERVICE_NAME, SERVICE_PORT);

        assertThat(baseURL).isNotBlank();
        testClient = WebTestClient
            .bindToServer()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .baseUrl(baseURL)
            .build();

        userAuthMap = new HashMap<>();
    }

    // NOTE 1 : Must use nested class since JUNIT 5 does not support multi file subtests
    // NOTE 2 : Tests will be executed from bottom to top

    @DisplayName("10. GIVEN existing board")
    @Nested
    class NestedBoardDeleteST extends BoardDeleteST {}

    @DisplayName("9. GIVEN board with existing column")
    @Nested
    class NestedColumnDeleteST extends ColumnDeleteST {}

    @DisplayName("8. GIVEN board with existing task")
    @Nested
    class NestedDeleteST extends TaskDeleteST {}

    @DisplayName("7. GIVEN board with empty task")
    @Nested
    class NestedTaskCreateST extends TaskCreateST {}

    @DisplayName("6. GIVEN board with empty column")
    @Nested
    class NestedColumnCreateST extends ColumnCreateST {}

    @DisplayName("5. GIVEN non existing board")
    @Nested
    class NestedBoardCreationST extends BoardCreationST {}

    @DisplayName("4. GIVEN logged out existing user")
    @Nested
    class NestedExistingUserST extends ExistingUserST {
    }

    @DisplayName("3. GIVEN logged in user")
    @Nested
    class NestedRefreshSessionST extends RefreshSessionST {
    }

    @DisplayName("2. GIVEN registered user")
    @Nested
    class NestedAuthenticationST extends AuthenticationST {
    }

    @DisplayName("1. GIVEN unregistered user")
    @Nested
    class NestedRegistrationST extends RegistrationST {
    }

}
