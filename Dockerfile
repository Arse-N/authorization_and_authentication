FROM maven:3.6.3-jdk-11 as build-stage
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:11-jre-slim AS  test
COPY --from=build-stage /app/target/*.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=test","/usr/local/lib/app.jar"]