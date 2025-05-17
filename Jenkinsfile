pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
        DB_HOST = 'db'
        DB_PORT = '3306'
        DOCKER_COMPOSE_VERSION = '2.21.0'
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Setup Tools') {
            steps {
                script {
                    // Instala Java 21
                    sh '''
                        echo "Verificando/Instalando Java 21..."
                        if ! command -v java &> /dev/null || ! java -version 2>&1 | grep -q "version \\"21"; then
                            echo "Instalando Java 21..."
                            if command -v apt-get &> /dev/null; then
                                # Debian/Ubuntu
                                apt-get update
                                apt-get install -y openjdk-21-jdk
                            elif command -v yum &> /dev/null; then
                                # RHEL/CentOS
                                yum install -y java-21-openjdk-devel
                            else
                                echo "Sistema não suportado para instalação automática do Java"
                                exit 1
                            fi
                        fi
                        
                        echo "Versão do Java:"
                        java -version
                    '''

                    // Verifica e instala Docker e Docker Compose
                    sh '''
                        echo "Verificando Docker..."
                        docker --version || {
                            echo "Erro: Docker não está instalado ou não está acessível"
                            exit 1
                        }

                        echo "Verificando/Instalando Docker Compose..."
                        if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
                            echo "Instalando Docker Compose..."
                            curl -L "https://github.com/docker/compose/releases/download/v${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
                            chmod +x /usr/local/bin/docker-compose
                            echo "Docker Compose instalado com sucesso"
                        fi

                        if command -v docker-compose &> /dev/null; then
                            echo "Usando docker-compose standalone:"
                            docker-compose --version
                        elif docker compose version &> /dev/null; then
                            echo "Usando docker compose plugin:"
                            docker compose version
                        else
                            echo "Erro: Docker Compose não está disponível mesmo após tentativa de instalação"
                            exit 1
                        fi
                    '''
                }
            }
        }

        stage('Start Containers') {
            steps {
                script {
                    sh '''
                        echo "Parando containers anteriores..."
                        if command -v docker-compose &> /dev/null; then
                            docker-compose -f ${COMPOSE_FILE} down -v || true
                        else
                            docker compose -f ${COMPOSE_FILE} down -v || true
                        fi

                        echo "Iniciando containers..."
                        if command -v docker-compose &> /dev/null; then
                            docker-compose -f ${COMPOSE_FILE} up -d
                        else
                            docker compose -f ${COMPOSE_FILE} up -d
                        fi

                        echo "Containers em execução:"
                        docker ps
                    '''
                }
            }
        }

        stage('Wait for MySQL') {
            steps {
                script {
                    sh '''
                        echo "Esperando o MySQL iniciar..."
                        for i in {1..30}; do
                            if docker ps -qf "name=java-db" | grep -q .; then
                                MYSQL_CONTAINER=$(docker ps -qf "name=java-db")
                                if docker exec $MYSQL_CONTAINER mysqladmin ping -h"localhost" --silent &> /dev/null; then
                                    echo "MySQL está pronto!"
                                    break
                                fi
                            fi
                            
                            if [ $i -eq 30 ]; then
                                echo "Timeout esperando MySQL. Logs do container:"
                                docker ps -a
                                docker logs $(docker ps -qf "name=java-db") || true
                                exit 1
                            fi
                            
                            echo "Aguardando banco... tentativa $i/30"
                            sleep 3
                        done
                    '''
                }
            }
        }

        stage('Build & Test') {
            steps {
                sh '''
                    echo "Ambiente Java:"
                    echo "JAVA_HOME: $JAVA_HOME"
                    java -version
                    
                    if [ -f "./mvnw" ]; then
                        chmod +x ./mvnw
                        export JAVA_HOME=${JAVA_HOME}
                        ./mvnw -v
                        ./mvnw clean verify -Dmaven.compiler.release=21
                    elif [ -f "./gradlew" ]; then
                        chmod +x ./gradlew
                        export JAVA_HOME=${JAVA_HOME}
                        ./gradlew -v
                        ./gradlew clean test
                    else
                        echo "Nem Maven Wrapper nem Gradle Wrapper encontrados!"
                        exit 1
                    fi
                '''
            }
        }

        stage('Stop Containers') {
            steps {
                script {
                    sh '''
                        echo "Parando containers..."
                        if command -v docker-compose &> /dev/null; then
                            docker-compose -f ${COMPOSE_FILE} down -v
                        else
                            docker compose -f ${COMPOSE_FILE} down -v
                        fi
                    '''
                }
            }
        }
    }

    post {
        always {
            echo "Limpeza final..."
            sh '''
                echo "Parando todos os containers..."
                if command -v docker-compose &> /dev/null; then
                    docker-compose -f ${COMPOSE_FILE} down -v || true
                else
                    docker compose -f ${COMPOSE_FILE} down -v || true
                fi
            '''
        }
        success {
            echo 'Pipeline executado com sucesso!'
        }
        failure {
            echo 'Pipeline falhou!'
            sh '''
                echo "=== Status dos Containers ==="
                docker ps -a
                
                echo "=== Logs do MySQL ==="
                if docker ps -qf "name=java-db" | grep -q .; then
                    docker logs $(docker ps -qf "name=java-db")
                else
                    echo "Container do MySQL não encontrado"
                fi
                
                echo "=== Logs da Aplicação ==="
                if docker ps -qf "name=java-app" | grep -q .; then
                    docker logs $(docker ps -qf "name=java-app")
                else
                    echo "Container da aplicação não encontrado"
                fi
            '''
        }
    }
}
