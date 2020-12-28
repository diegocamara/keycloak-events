package com.example.users.application.messaging.rabbitmq.model;

import com.example.users.domain.model.NewUser;
import com.example.users.domain.model.UpdateUserInfo;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class UserEvent {

  public static final String USERNAME_REPRENSENTATION_KEY = "username";
  public static final String EMAIL_REPRESENTATION_KEY = "email";

  public enum OperationType {
    CREATE,
    UPDATE,
    DELETE,
    ACTION;
  }

  private UUID id;
  private OperationType operation;
  private Map<String, Object> representation;

  public NewUser toNewUser() {
    return new NewUser(this.id, get(USERNAME_REPRENSENTATION_KEY), get(EMAIL_REPRESENTATION_KEY));
  }

  public UpdateUserInfo toUpdateUserInfo() {
    return new UpdateUserInfo(
        this.id, get(USERNAME_REPRENSENTATION_KEY), get(EMAIL_REPRESENTATION_KEY));
  }

  private String get(String representationKey) {
    if (representation == null) {
      return null;
    }
    var value = representation.get(representationKey);
    return value != null ? (String) value : null;
  }
}
