FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -DskipTests
EXPOSE 8080

# Garante que o JAR existe e está no local correto
RUN ls -la target/planejador_horario-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/target/planejador_horario-0.0.1-SNAPSHOT.jar"]
