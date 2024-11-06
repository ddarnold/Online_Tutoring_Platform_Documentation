FROM eclipse-temurin:21.0.2_13-jdk-jammy AS builder
WORKDIR /opt/app

# Copy necessary configuration and executable files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Download dependencies and cache them
RUN ./mvnw dependency:go-offline -B

# Copy the source code and build the project
COPY ./src ./src
RUN ./mvnw clean install -Dmaven.test.skip=true -B

FROM eclipse-temurin:21.0.2_13-jre-jammy AS final
WORKDIR /opt/app
EXPOSE 8080

# Replace the wildcard with the actual jar name if known
COPY --from=builder /opt/app/target/thutorium-0.0.1-SNAPSHOT.jar /opt/app/app.jar
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]