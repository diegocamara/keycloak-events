package com.example.users.infrastructure.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.AllArgsConstructor;
import org.bson.UuidRepresentation;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.SpringDataMongoDB;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import static java.util.Collections.singletonList;

@Configuration
@AllArgsConstructor
public class MongoDBConfiguration extends AbstractReactiveMongoConfiguration {

  private final MongoProperties mongoProperties;

  @Override
  public MongoClient reactiveMongoClient() {
    final var settings =
        MongoClientSettings.builder()
            .applyToClusterSettings(builder -> builder.hosts(singletonList(serverAddress())))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build();
    return MongoClients.create(settings, SpringDataMongoDB.driverInformation());
  }

  private ServerAddress serverAddress() {
    return new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort());
  }

  @Override
  protected String getDatabaseName() {
    return this.mongoProperties.getDatabase();
  }
}
