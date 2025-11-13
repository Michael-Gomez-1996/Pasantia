# Usar una imagen base de Java
FROM openjdk:17-jdk-slim

# Crear directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR (asegúrate de que tu proyecto esté compilado)
COPY target/*.jar app.jar

# Exponer el puerto que usa Render
EXPOSE 10000

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]