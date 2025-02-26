FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

# Copy the POM file
COPY java/pom.xml .

# Copy the source code
COPY java/com ./com

# Build the application
RUN mvn clean package

# Run the application
CMD ["java", "-jar", "target/mars-simulator-backend-1.0-SNAPSHOT.jar"]

