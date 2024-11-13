# Usa una imagen oficial de OpenJDK como imagen base 
FROM openjdk:11-jre-slim 
# Establece el directorio de trabajo en /app 
WORKDIR /app 
# Copia el archivo JAR generado al contenedor 
COPY target/ferreteria-api-1.0-SNAPSHOT.jar /app/ferreteria-api.jar
# Expone el puerto 8080 
EXPOSE 8080 
# Define el comando para ejecutar la aplicación 
ENTRYPOINT ["java", "-jar", "ferreteria-api.jar"]
