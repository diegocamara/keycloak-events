package com.example.users.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {
  private UUID id;
  private String username;
  private String email;

  public User toUser() {
    return new User(this.id, this.username, this.email);
  }
}
