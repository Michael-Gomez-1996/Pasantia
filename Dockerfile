# Usar una imagen base de Java más específica
FROM openjdk:17

# Crear directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR
COPY target/*.jar app.jar

# Exponer el puerto
EXPOSE 10000

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]