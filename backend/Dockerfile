FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","backend.jar"]
