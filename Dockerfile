# First stage: complete build environment
FROM maven:3.8.5-jdk-11 AS builder
ADD ./pom.xml pom.xml
ADD ./src src/
RUN mvn clean package -DskipTests

# Second stage: minimal runtime environment
From openjdk:13-alpine
COPY --from=builder target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]