# Usar Eclipse Temurin - la más confiable en Render
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar el archivo JAR
COPY target/*.jar app.jar

# Exponer el puerto
EXPOSE 10000

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]