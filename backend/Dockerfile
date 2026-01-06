# ===============================
# Stage 1: Build the Application
# ===============================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# 1. Copy the pom.xml first to cache dependencies
COPY pom.xml .
# Download dependencies (this layer will be cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# 2. Copy the source code
COPY src ./src

# 3. Build the JAR (skipping tests to speed up container build)
RUN mvn clean package -DskipTests

# ===============================
# Stage 2: Run the Application
# ===============================
FROM eclipse-temurin:21-jre-alpine

# Good practice for temp file storage (Tomcat uses this)
VOLUME /tmp

# Copy the built jar from the 'build' stage
# Adjust the name if your final jar isn't strictly 'app.jar' logic
#WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose the internal port (Documentation only, Compose handles the real mapping)
EXPOSE 3999

# The command to run the app
ENTRYPOINT ["java", "-jar", "/app.jar"]