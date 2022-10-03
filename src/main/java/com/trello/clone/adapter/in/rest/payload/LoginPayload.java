package com.trello.clone.adapter.in.rest.payload;

import javax.validation.constraints.NotBlank;

public record LoginPayload(String username, String email, @NotBlank(message = "com.trello.clone.password.required") String password) {
}
