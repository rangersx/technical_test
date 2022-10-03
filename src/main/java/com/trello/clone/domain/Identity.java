package com.trello.clone.domain;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Domain object for authentication of a user
 */
@Getter
@RequiredArgsConstructor
@Slf4j
@ToString
@EqualsAndHashCode
public class Identity {
    private final IdentityId id;
    private final String username;
    private final String emailAddress;
    private final UserId user;

    private String password;
    private UUID sessionId;

    public Identity setPassword(@NonNull String password) {
        this.password = password;
        return this;
    }

    public Identity setSessionId(@NonNull UUID sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public boolean passwordMatches(@NonNull String password) {
        return StringUtils.equals(password, this.password);
    }

    public Identity invalidateSession() {
        this.sessionId = null;
        return this;
    }

    public Identity newSession() {
        this.sessionId = UUID.randomUUID();
        return this;
    }

    public boolean isSessionExist() {
        return this.sessionId != null;
    }

    public boolean isSessionNotExist() {
        return !isSessionExist();
    }

    public boolean isSessionValid(@NonNull UUID sessionId) {
        return isSessionExist() && sessionId.equals(this.sessionId);
    }
}
