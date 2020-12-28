package com.example.users.domain.feature;

import com.example.users.domain.model.UpdateUserInfo;
import com.example.users.domain.model.User;
import reactor.core.publisher.Mono;

public interface UpdateUser {
  Mono<User> handle(UpdateUserInfo updateUserInfo);
}
