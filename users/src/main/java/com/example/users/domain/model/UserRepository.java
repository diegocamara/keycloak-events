package com.example.users.domain.model;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
  Mono<Void> save(User user);

  Mono<User> find(UUID id);

  Mono<Void> delete(UUID id);
}
