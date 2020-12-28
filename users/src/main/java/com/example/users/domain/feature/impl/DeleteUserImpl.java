package com.example.users.domain.feature.impl;

import com.example.users.domain.feature.DeleteUser;
import com.example.users.domain.model.UserRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
public class DeleteUserImpl implements DeleteUser {

  private final UserRepository userRepository;

  @Override
  public Mono<Void> handle(UUID id) {
    return userRepository.delete(id);
  }
}
