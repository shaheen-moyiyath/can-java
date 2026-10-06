# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build production jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Add non-root user for security
RUN addgroup -S canteen && adduser -S canteen -G canteen
USER canteen:canteen

# Copy jar from builder stage
COPY --from=builder /app/target/canteen-ordering-system-1.0.0.jar app.jar

# Expose default HTTP port
EXPOSE 8080

# Environment defaults
ENV SPRING_PROFILES_ACTIVE=default \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
