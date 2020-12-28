package com.example.provider.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEvent {

  public enum OperationType {
    CREATE,
    UPDATE,
    DELETE,
    ACTION;
  }

  private UUID id;
  private OperationType operation;
  private Map<String, Object> representation;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public OperationType getOperation() {
    return operation;
  }

  public void setOperation(OperationType operation) {
    this.operation = operation;
  }

  public Map<String, Object> getRepresentation() {
    return representation;
  }

  public void setRepresentation(Map<String, Object> representation) {
    this.representation = representation;
  }
}
