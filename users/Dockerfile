FROM gradle:6.7.1-jdk15 as build
WORKDIR /app
COPY . .
RUN gradle clean build

FROM openjdk:15-jdk-alpine
COPY --from=build /app/build/libs/*.jar users.jar
ENTRYPOINT ["java", "-jar", "/users.jar"]