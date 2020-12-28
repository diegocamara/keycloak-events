package com.example.users.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class User {
  private UUID id;
  private String username;
  private String email;
}
