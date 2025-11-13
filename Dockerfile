# Usar Eclipse Temurin con JDK para poder compilar
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiar el código fuente
COPY . .

# Compilar el proyecto
RUN ./mvnw clean package -DskipTests

# Copiar el JAR compilado
COPY target/*.jar app.jar

# Exponer el puerto
EXPOSE 10000

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]