package com.example.users.domain.feature;

import com.example.users.domain.model.NewUser;
import com.example.users.domain.model.User;
import reactor.core.publisher.Mono;

public interface CreateUser {

  Mono<User> handle(NewUser newUser);
}
