FROM openjdk:11

WORKDIR /app

# Actualiza la ruta y el nombre del JAR según el nuevo nombre del proyecto
COPY target/API-1.0-SNAPSHOT.jar /app/api.jar

# Añadir comandos de depuración para verificar el contenido
RUN ls -la /app && echo "Contents of /app listed"

# Exponer el puerto de la aplicación
EXPOSE 8080

# Definir el comando de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/api.jar"]

