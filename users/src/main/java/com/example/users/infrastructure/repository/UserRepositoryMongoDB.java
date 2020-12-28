package com.example.users.infrastructure.repository;

import com.example.users.domain.model.User;
import com.example.users.domain.model.UserRepository;
import com.example.users.infrastructure.repository.springdata.mongodb.UserRepositorySpringDataReactive;
import com.example.users.infrastructure.repository.springdata.mongodb.document.UserDocument;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserRepositoryMongoDB implements UserRepository {

  private final UserRepositorySpringDataReactive userRepositorySpringDataReactive;

  @Override
  public Mono<Void> save(User user) {
    return userRepositorySpringDataReactive.save(new UserDocument(user)).then();
  }

  @Override
  public Mono<User> find(UUID id) {
    return userRepositorySpringDataReactive.findById(id).map(UserDocument::toUser);
  }

  @Override
  public Mono<Void> delete(UUID id) {
    return userRepositorySpringDataReactive.deleteById(id);
  }
}
