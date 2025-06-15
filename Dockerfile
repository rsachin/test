# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy only the POM file first to leverage Docker cache
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set environment variables with defaults
ENV SPRING_PROFILES_ACTIVE=prod
ENV DB_URL=jdbc:postgresql://postgres:5432/propertydb
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres
ENV SERVER_PORT=8080
ENV MANAGEMENT_SERVER_PORT=8081

# Expose ports
EXPOSE ${SERVER_PORT} ${MANAGEMENT_SERVER_PORT}

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
