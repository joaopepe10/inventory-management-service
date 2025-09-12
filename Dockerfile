# Etapa 1: Construir a aplicação
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY src/main/resources .
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final para rodar o JAR
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
