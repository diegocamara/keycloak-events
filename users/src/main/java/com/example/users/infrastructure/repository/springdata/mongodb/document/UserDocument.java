package com.example.users.infrastructure.repository.springdata.mongodb.document;

import com.example.users.domain.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "#{@environment.getProperty('collections.users')}")
public class UserDocument {
  @Id private UUID id;
  private String username;
  private String email;

  public UserDocument(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
  }

  public User toUser() {
    return new User(this.id, this.username, this.email);
  }
}
