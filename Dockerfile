# Step 1: Build the JAR file using Maven
FROM maven:latest AS build

WORKDIR /app

# Copy only the pom.xml first to leverage Docker cache for dependencies
COPY pom.xml .

# Download dependencies to cache them in a layer
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the JAR file using Maven (this will create the target/ directory inside the container)
RUN mvn clean package -DskipTests

# Step 2: Create the runtime image with OpenJDK
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy the JAR file from the build stage into the runtime image
COPY --from=build /app/target/CocktailVaultAPI-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]