package com.example.users.domain.feature;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeleteUser {
  Mono<Void> handle(UUID id);
}
