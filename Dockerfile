FROM eclipse-temurin:21-jdk

WORKDIR /app

# Instala ferramentas de diagnóstico
RUN apt-get update && apt-get install -y \
    iputils-ping \
    dnsutils \
    net-tools \
    curl \
    netcat-openbsd \
    tree \
    && rm -rf /var/lib/apt/lists/*

# Copia arquivos de build primeiro
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Ajusta permissões do mvnw
RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline

# Copia o código fonte e verifica a estrutura
COPY src ./src
RUN tree src/main/java/br/com/leonardo/planejador_horario

# Executa o build com mais detalhes de debug
RUN ./mvnw clean package -DskipTests -X

# Copia o arquivo de configuração atualizado
COPY src/main/resources/application.properties /app/target/classes/application.properties

# Cria script de healthcheck com permissões corretas
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

# Verifica se o JAR foi criado corretamente
RUN ls -la target/planejador_horario-0.0.1-SNAPSHOT.jar

EXPOSE 9090

HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD /app/healthcheck.sh

ENTRYPOINT ["java", "-jar", "/app/target/planejador_horario-0.0.1-SNAPSHOT.jar"]


#   docker-compose down
#   docker-compose build
#   docker-compose up --build