# Use Eclipse Temurin 17 as base image (with Maven installed)
FROM eclipse-temurin:17-jdk-focal

# Set the working directory inside the container
WORKDIR /app

# Install Maven (since it’s not included by default in Temurin images)
RUN apt-get update && apt-get install -y maven

# Copy the pom.xml file (and any other files needed for the build)
COPY pom.xml ./

# Download the dependencies (offline mode) to cache them in Docker layers
RUN mvn dependency:go-offline

# Copy the application source code
COPY src ./src

# Run the Spring Boot application using `mvn`
CMD ["mvn", "spring-boot:run"]
