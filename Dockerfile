FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Set executable permission on the mvnw script
RUN chmod +x mvnw

COPY src ./src

# Run Maven build
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 9093

#ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-Xms256m", "-Xmx256m", "-jar", "app.jar"]
