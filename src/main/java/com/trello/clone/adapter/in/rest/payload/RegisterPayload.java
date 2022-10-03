package com.trello.clone.adapter.in.rest.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record RegisterPayload(
    @Size(min = 2, max = 50, message = "com.trello.clone.username.min.2.max.50")
    @NotBlank String username,

    @Email(message = "com.trello.clone.email.invalid")
    @NotBlank String email,

    @Size(min = 6, message = "com.trello.clone.password.min.6")
    @NotBlank String password,

    String firstName,
    String lastName
) {
}
