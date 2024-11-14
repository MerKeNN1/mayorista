FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/ferreteria-api-1.0-SNAPSHOT.jar /app/ferreteria-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/ferreteria-api.jar"]
