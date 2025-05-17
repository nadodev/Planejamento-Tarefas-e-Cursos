FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia arquivos de build primeiro
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copia o código fonte
COPY src ./src

# Executa o build
RUN ./mvnw package -DskipTests

# Copia o arquivo de configuração atualizado
COPY src/main/resources/application.properties /app/target/classes/application.properties

EXPOSE 8080

# Garante que o JAR existe e está no local correto
RUN ls -la target/planejador_horario-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/target/planejador_horario-0.0.1-SNAPSHOT.jar"]
