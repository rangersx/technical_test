package com.trello.clone.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Domain object for user profile
 */
@Getter
@RequiredArgsConstructor
@Slf4j
@ToString
@EqualsAndHashCode
public class User {
    private final UserId id;

    @Setter
    private String lastName;

    @Setter
    private String firstName;

    public String getInitials() {
        return (firstName.charAt(0) + lastName.substring(0, 1)).toUpperCase();
    }
}

