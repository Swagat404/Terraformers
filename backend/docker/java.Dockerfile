# Use a Maven base image that includes JDK 17
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set the working directory for the build stage INSIDE the java subfolder structure
WORKDIR /build-app

# Copy the POM file FROM the 'java' subdirectory in the context
# to the WORKDIR /build-app
COPY java/pom.xml .

# Copy the Maven wrapper files FROM the 'java' subdirectory (if used)
# COPY java/.mvn/ .mvn ./mvn/

# Copy the entire source code directory FROM the context's 'java/src' folder
# to the WORKDIR's 'src' folder (/build-app/src)
COPY java/src ./src

# Build the application using Maven inside the container
# The WORKDIR is already /build-app where pom.xml was copied
RUN mvn package -DskipTests

# --- Runtime Stage ---
# Use a smaller JRE base image for the final application container
FROM eclipse-temurin:17-jre-alpine

# Set the working directory for the runtime stage
WORKDIR /app

# Copy *only* the built JAR file from the 'builder' stage's target directory
# Adjust the JAR name if it's different
COPY --from=builder /build-app/target/mars-simulator-backend-1.0-SNAPSHOT.jar app.jar

# Expose the port the application runs on (e.g., 8080)
EXPOSE 8080

# Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app/app.jar"]