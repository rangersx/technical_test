package com.trello.clone.application.port.in.command;

//https://www.morling.dev/blog/enforcing-java-record-invariants-with-bean-validation/

/**
 * Registration command
 */
public record RegistrationCommand(String username, String email, String password, String firstName, String lastName) {
}
