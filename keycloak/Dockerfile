FROM gradle:6.7.1-jdk15 as build
WORKDIR /app
COPY spi/event-emitter-spi/ .
RUN gradle clean build

FROM quay.io/keycloak/keycloak:12.0.1
ARG REALM_FILE=realms/apprealm.json
ENV KEYCLOAK_IMPORT=${REALM_FILE}
COPY ${REALM_FILE} /realms/
COPY spi/setup.cli /opt/jboss/startup-scripts/
COPY --from=build /app/build/libs/*.jar /opt/jboss/keycloak/standalone/deployments