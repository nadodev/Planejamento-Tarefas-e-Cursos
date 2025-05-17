pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
        DB_HOST = 'db'
        DB_PORT = '3306'
        DOCKER_COMPOSE_VERSION = '2.21.0'
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk-amd64'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Setup Tools') {
            steps {
                script {
                    // Instala Java 21
                    sh '''
                        echo "Verificando/Instalando Java 21..."
                        
                        # Função para instalar Java no Ubuntu/Debian
                        install_java_debian() {
                            echo "Adicionando repositório para Java 21..."
                            apt-get update
                            apt-get install -y wget apt-transport-https
                            wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | apt-key add -
                            echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list
                            apt-get update
                            apt-get install -y temurin-21-jdk
                            update-alternatives --set java /usr/lib/jvm/temurin-21-jdk-amd64/bin/java
                        }
                        
                        # Função para instalar Java no CentOS/RHEL
                        install_java_rhel() {
                            echo "Adicionando repositório para Java 21..."
                            curl -L https://packages.adoptium.net/artifactory/api/rpm/rhel/openj9/9/x86_64/adoptium.repo -o /etc/yum.repos.d/adoptium.repo
                            yum install -y temurin-21-jdk
                        }
                        
                        # Função para instalar Java manualmente
                        install_java_manual() {
                            echo "Instalando Java 21 manualmente..."
                            cd /tmp
                            wget https://download.java.net/java/GA/jdk21.0.2/f2283984656d49d69e91c558476027ac/13/GPL/openjdk-21.0.2_linux-x64_bin.tar.gz
                            tar xzf openjdk-21.0.2_linux-x64_bin.tar.gz
                            mkdir -p /usr/lib/jvm
                            mv jdk-21.0.2 /usr/lib/jvm/java-21-openjdk-amd64
                            update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-21-openjdk-amd64/bin/java 1
                            update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/java-21-openjdk-amd64/bin/javac 1
                            echo "Java 21 instalado em /usr/lib/jvm/java-21-openjdk-amd64"
                        }
                        
                        # Verifica se Java 21 já está instalado
                        if java -version 2>&1 | grep -q "version \\"21"; then
                            echo "Java 21 já está instalado"
                            java -version
                        else
                            echo "Instalando Java 21..."
                            # Tenta diferentes métodos de instalação
                            if command -v apt-get &> /dev/null; then
                                install_java_debian || install_java_manual
                            elif command -v yum &> /dev/null; then
                                install_java_rhel || install_java_manual
                            else
                                install_java_manual
                            fi
                            
                            # Verifica a instalação
                            if ! java -version 2>&1 | grep -q "version \\"21"; then
                                echo "Falha na instalação do Java 21"
                                exit 1
                            fi
                        fi
                        
                        echo "Versão do Java instalada:"
                        java -version
                        echo "JAVA_HOME: ${JAVA_HOME}"
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
