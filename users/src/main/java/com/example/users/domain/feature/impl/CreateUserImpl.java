package com.example.users.domain.feature.impl;

import com.example.users.domain.feature.CreateUser;
import com.example.users.domain.model.NewUser;
import com.example.users.domain.model.User;
import com.example.users.domain.model.UserRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CreateUserImpl implements CreateUser {

  private final UserRepository userRepository;

  @Override
  public Mono<User> handle(NewUser newUser) {
    final var user = newUser.toUser();
    return userRepository.save(user).then(Mono.just(user));
  }
}
