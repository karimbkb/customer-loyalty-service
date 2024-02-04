FROM maven:3.9.6-amazoncorretto-17-al2023 AS builder
COPY ./../src /home/app/src
COPY ./../pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17-jdk-slim
RUN mkdir /opt/app
COPY --from=builder /home/app/target/customer-loyalty-service-0.0.1-SNAPSHOT.jar /opt/app/app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/opt/app/app.jar" ]