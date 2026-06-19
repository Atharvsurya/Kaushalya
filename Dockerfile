# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies to make future Render builds 10x faster
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code (including your frontend static files) and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
# Using JRE instead of JDK for a lighter, faster image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the exact compiled jar from the /app folder in Stage 1
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# Boot up with your excellent memory constraints!
ENTRYPOINT ["java", "-Xmx300m", "-Xms300m", "-jar", "app.jar"]