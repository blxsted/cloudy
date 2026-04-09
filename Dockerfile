# Build Stage with Tests
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY . .

# Run tests and build (tests require docker socket for testcontainers)
ARG SKIP_TESTS=false
RUN mvn clean package -DskipTests=${SKIP_TESTS}

# Run Stage
FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]