FROM maven:3.9.9-amazoncorretto-8-al2023 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Use OpenJDK to run the application
FROM openjdk:17-slim-bullseye

WORKDIR /app

COPY --from=build /app/target/spring-boot-starter-parent-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]
