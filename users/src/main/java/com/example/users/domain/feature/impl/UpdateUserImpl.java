package com.example.users.domain.feature.impl;

import com.example.users.domain.feature.UpdateUser;
import com.example.users.domain.model.UpdateUserInfo;
import com.example.users.domain.model.User;
import com.example.users.domain.model.UserRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class UpdateUserImpl implements UpdateUser {

  private final UserRepository userRepository;

  @Override
  public Mono<User> handle(UpdateUserInfo updateUserInfo) {
    final var userId = updateUserInfo.getId();
    return userRepository
        .find(userId)
        .doOnNext(user -> updateUser(user, updateUserInfo))
        .flatMap(user -> userRepository.save(user).thenReturn(user));
  }

  private void updateUser(User user, UpdateUserInfo updateUserInfo) {
    user.setUsername(updateUserInfo.getUsername());
    user.setEmail(updateUserInfo.getEmail());
  }
}
