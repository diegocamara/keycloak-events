package com.example.users.infrastructure.repository.springdata.mongodb;

import com.example.users.infrastructure.repository.springdata.mongodb.document.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface UserRepositorySpringDataReactive
    extends ReactiveMongoRepository<UserDocument, UUID> {}
