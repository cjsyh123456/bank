# Build stage
FROM maven:3.8.6-openjdk-21 AS build
WORKDIR /app

# Copy POM first to download dependencies separately
COPY pom.xml .
RUN mvn dependency:resolve

# Then copy source code and build
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage
FROM openjdk:21-jdk-slim
WORKDIR /app

# Install health check tools and clean cache
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser && \
    chown appuser:appuser /app
USER appuser

# Copy JAR file from build stage
COPY --from=build /app/target/bank-transaction-service-*.jar app.jar

# Expose port
EXPOSE 8080

# run command
ENTRYPOINT ["java", "-jar", "app.jar"]