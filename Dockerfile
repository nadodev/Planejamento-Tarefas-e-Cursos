FROM eclipse-temurin:21-jdk

WORKDIR /app

# Instala ferramentas de diagnóstico
RUN apt-get update && apt-get install -y \
    iputils-ping \
    dnsutils \
    net-tools \
    curl \
    netcat-openbsd \
    && rm -rf /var/lib/apt/lists/*

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

# Cria script de healthcheck
RUN echo '#!/bin/sh\n\
echo "Verificando MySQL..."\n\
if nc -z -v mysql-planejador 3306; then\n\
    echo "MySQL está acessível"\n\
else\n\
    echo "MySQL não está acessível"\n\
    exit 1\n\
fi\n\
echo "Todos os checks passaram"\n\
exit 0' > /app/healthcheck.sh && chmod +x /app/healthcheck.sh

EXPOSE 8080

# Garante que o JAR existe e está no local correto
RUN ls -la target/planejador_horario-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/target/planejador_horario-0.0.1-SNAPSHOT.jar"]
