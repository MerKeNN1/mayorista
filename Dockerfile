FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/ferreteria-api-1.0-SNAPSHOT.jar /app/ferreteria-api.jar

RUN ls -la /app && cat /app/ferreteria-api.jar

ENTRYPOINT ["java", "-jar", "/app/ferreteria-api.jar"]
