# Multi-stage Dockerfile
# Stage 1: Build with JDK
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Copy maven wrapper & pom to leverage layer caching
COPY .mvn .mvn
COPY pom.xml mvnw mvnw.cmd ./
COPY src ./src

# Build the application (skip tests to speed up builds; adjust as needed)
RUN ./mvnw clean package -DskipTests

# Stage 2: Minimal runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the packaged jar from the builder stage
COPY --from=builder /app/target/library-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
