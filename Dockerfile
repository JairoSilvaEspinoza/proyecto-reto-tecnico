# -------- STAGE 1: BUILD --------
FROM eclipse-temurin:8u462-b08-jdk AS builder

WORKDIR /app

# Copiamos configuración de Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Descargamos dependencias (cache)
RUN ./mvnw dependency:go-offline -B

# Copiamos el código fuente
COPY src src

# Construimos el JAR
RUN ./mvnw clean package -DskipTests -B

# -------- STAGE 2: RUNTIME --------
FROM eclipse-temurin:8u462-b08-jdk

WORKDIR /app

# Copiamos solo el JAR desde el stage anterior
COPY --from=builder /app/target/proyecto-reto-tecnico-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto de la aplicación
EXPOSE 8080

# Comando para iniciar la app
ENTRYPOINT ["java","-jar","app.jar"]
