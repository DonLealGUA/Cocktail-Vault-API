# Step 1: Build the JAR file using Maven
FROM maven:latest AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Create the runtime image with OpenJDK
FROM openjdk:17-jdk-alpine
WORKDIR /app
# Copy the JAR file into the runtime image
COPY --from=build /app/target/CocktailVaultAPI-0.0.1-SNAPSHOT.jar app.jar
# Use the same entrypoint but ensure the JAR is copied correctly
ENTRYPOINT ["java", "-jar", "app.jar"]
