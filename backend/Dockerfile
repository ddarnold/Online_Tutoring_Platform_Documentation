FROM maven:3.9.9-eclipse-temurin-21 AS build
COPY . .
RUN mvn install clean package -DskipTests

FROM eclipse-temurin:21-alpine
RUN mkdir /opt/app
COPY --from=build /target/*.jar backend.jar
EXPOSE 8080:8080
CMD ["java", "-jar", "/opt/app/japp.jar"]
ENTRYPOINT ["java","-jar","backend.jar"]
