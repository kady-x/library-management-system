# Use Eclipse Temurin as OpenJDK 17 development kit
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build file to leverage Docker layer caching
COPY .mvn .mvn
COPY pom.xml mvnw* ./
COPY src ./src

# Duild the application
RUN ./mvnw clean package -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Run the application
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/library-0.0.1-SNAPSHOT.jar"]
