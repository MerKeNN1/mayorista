<<<<<<< HEAD
FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/ferreteria-api-1.0-SNAPSHOT.jar /app/ferreteria-api.jar

RUN ls -la /app && cat /app/ferreteria-api.jar

=======
FROM openjdk:11-jre-slim 
WORKDIR /app 
COPY target/ferreteria-api-1.0-SNAPSHOT.jar /app/ferreteria-api.jar 
EXPOSE 8080 
RUN ls -la /app && echo "Contents of /app listed"
>>>>>>> 6c7548bb7910c3bed8212e043b51f791338a2284
ENTRYPOINT ["java", "-jar", "/app/ferreteria-api.jar"]
