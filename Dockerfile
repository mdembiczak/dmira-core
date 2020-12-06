FROM openjdk:11-jre-slim
WORKDIR /app

COPY ./target/*.jar ./app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom" ,"-jar", "/app/app.jar"]

EXPOSE 8080