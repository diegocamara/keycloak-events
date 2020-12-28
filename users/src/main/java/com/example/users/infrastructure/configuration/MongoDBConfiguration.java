package com.example.users.infrastructure.configuration;

import com.mongodb.MongoClientSettings;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

// @Configuration
public class MongoDBConfiguration extends AbstractReactiveMongoConfiguration {

  @Value("${spring.data.mongodb.database}")
  private String databaseName;

  @Override
  protected void configureClientSettings(MongoClientSettings.Builder builder) {
    builder.uuidRepresentation(UuidRepresentation.STANDARD);
  }

  @Override
  protected String getDatabaseName() {
    return this.databaseName;
  }
}
