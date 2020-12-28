package com.example.users.infrastructure.configuration;

import com.example.users.domain.feature.CreateUser;
import com.example.users.domain.feature.DeleteUser;
import com.example.users.domain.feature.UpdateUser;
import com.example.users.domain.feature.impl.CreateUserImpl;
import com.example.users.domain.feature.impl.DeleteUserImpl;
import com.example.users.domain.feature.impl.UpdateUserImpl;
import com.example.users.domain.model.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsersConfiguration {

  @Bean
  public CreateUser createUser(UserRepository userRepository) {
    return new CreateUserImpl(userRepository);
  }

  @Bean
  public UpdateUser updateUser(UserRepository userRepository) {
    return new UpdateUserImpl(userRepository);
  }

  @Bean
  public DeleteUser deleteUser(UserRepository userRepository) {
    return new DeleteUserImpl(userRepository);
  }
}
