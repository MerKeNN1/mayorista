FROM openjdk:11-jre-slim 
WORKDIR /app 
COPY target/ferreteria-api-1.0-SNAPSHOT.jar /app/ferreteria-api.jar 
EXPOSE 8080 
RUN ls -la /app && echo "Contents of /app listed"
ENTRYPOINT ["java", "-jar", "/app/ferreteria-api.jar"]
