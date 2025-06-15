# Property Listing Service

A Spring Boot microservice for managing property listings with RESTful API endpoints.

## Features

- RESTful API for property management
- H2 in-memory database for development and testing
- PostgreSQL support for production
- OpenAPI (Swagger) documentation
- Comprehensive test coverage
- Multiple environment configurations
- Actuator endpoints for monitoring
- Input validation
- Global exception handling
- Pagination and filtering

## Prerequisites

- Java 17 or later
- Maven 3.6 or later
- Docker (optional, for running PostgreSQL in production)

## Environment Configurations

The application supports three profiles:

1. **dev** - Development environment (default)
   - H2 in-memory database
   - H2 Console enabled at `/h2-console`
   - Detailed logging
   - Auto DDL update

2. **test** - Test environment
   - H2 in-memory database with create-drop
   - Minimal logging
   - Used for integration tests

3. **prod** - Production environment
   - PostgreSQL database (configure via environment variables)
   - Connection pooling
   - Production-ready settings
   - Actuator endpoints for monitoring

## Running the Application

### Development Mode (Default)

```bash
# Using Maven
mvn spring-boot:run

# Or build and run the JAR
mvn clean package
java -jar target/property-listing-service-0.0.1-SNAPSHOT.jar
```

Access the application at: http://localhost:8080

### Test Mode

```bash
# Run with test profile
mvn spring-boot:run -Dspring-boot.run.profiles=test

# Or using environment variable
SPRING_PROFILES_ACTIVE=test mvn spring-boot:run
```

### Production Mode

1. First, start a PostgreSQL database:

```bash
docker run --name property-db -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=propertydb -p 5432:5432 -d postgres:13
```

2. Run the application with production profile:

```bash
# Using environment variables
export DB_URL=jdbc:postgresql://localhost:5432/propertydb
export DB_USERNAME=postgres
export DB_PASSWORD=postgres

mvn spring-boot:run -Dspring-boot.run.profiles=prod

# Or using command line arguments
mvn spring-boot:run -Dspring-boot.run.profiles=prod \
    -Dspring.datasource.url=jdbc:postgresql://localhost:5432/propertydb \
    -Dspring.datasource.username=postgres \
    -Dspring.datasource.password=postgres
```

## API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PropertyServiceTest

# Run with test profile (uses H2 in-memory DB)
mvn test -Dspring.profiles.active=test

# Run with coverage (using JaCoCo)
mvn clean test jacoco:report
```

## Building a Docker Image

1. Build the application:
```bash
mvn clean package
```

2. Build the Docker image:
```bash
docker build -t property-listing-service:latest .
```

3. Run the container:
```bash
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/propertydb \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  --name property-service \
  property-listing-service:latest
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| SPRING_PROFILES_ACTIVE | Active profile (dev, test, prod) | dev |
| DB_URL | Database URL | jdbc:h2:mem:propertydb |
| DB_USERNAME | Database username | sa |
| DB_PASSWORD | Database password | |
| SERVER_PORT | Server port | 8080 |
| MANAGEMENT_SERVER_PORT | Actuator port | 8081 |

## Actuator Endpoints

When running with production profile, these endpoints are available:

- Health: http://localhost:8081/actuator/health
- Info: http://localhost:8081/actuator/info
- Metrics: http://localhost:8081/actuator/metrics
- Prometheus: http://localhost:8081/actuator/prometheus

## License

This project is licensed under the MIT License.
