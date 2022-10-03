package com.trello.clone.domain;

import java.util.UUID;

import org.springframework.lang.NonNull;

public record IdentityId(@NonNull UUID id) {}
