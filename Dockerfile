# Using multi-stage build:
# - First stage builds the project, contains all the necessary libraries and tools for that
# - Second stage generate the final image, which is smaller, containing the basic building blocks and the app itself

# STAGE 1: Build project

FROM maven:3.8.5-openjdk-17 AS builder
COPY src /building/src
COPY pom.xml /building
RUN mvn -f /building/pom.xml clean package


# STAGE 2: Generate final image

FROM openjdk:17-alpine
COPY --from=builder /building/target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
