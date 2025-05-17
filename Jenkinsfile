pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
        DB_HOST = 'db'
        DB_PORT = '3306'
        APP_PORT = '9090'
        DOCKER_COMPOSE_VERSION = '2.21.0'
        JAVA_HOME = '/usr/lib/jvm/temurin-21-jdk-amd64'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Setup Tools') {
            steps {
                script {
                    // Instala Java 21
                    sh '''
                        echo "Verificando/Instalando Java 21..."
                        
                        # Função para verificar a versão do Java
                        check_java_version() {
                            echo "Verificando versão do Java..."
                            if command -v java &> /dev/null; then
                                echo "Comando java encontrado. Verificando versão:"
                                java -version
                                JAVA_VERSION=$(java -version 2>&1 | head -n 1)
                                echo "Versão completa: $JAVA_VERSION"
                                if echo "$JAVA_VERSION" | grep -E "version.*21"; then
                                    echo "Java 21 detectado com sucesso"
                                    return 0
                                else
                                    echo "Versão do Java não é 21"
                                    return 1
                                fi
                            else
                                echo "Comando java não encontrado"
                                return 1
                            fi
                        }
                        
                        # Função para instalar Java no Ubuntu/Debian
                        install_java_debian() {
                            echo "Instalando Java 21 via Adoptium (Debian/Ubuntu)..."
                            apt-get update
                            apt-get install -y wget apt-transport-https gnupg
                            wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | tee /etc/apt/trusted.gpg.d/adoptium.asc
                            echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= \'/^VERSION_CODENAME/{print$2}\' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list
                            apt-get update
                            apt-get install -y temurin-21-jdk
                            
                            # Configura as alternativas do Java
                            echo "Configurando alternativas do Java..."
                            update-alternatives --install /usr/bin/java java /usr/lib/jvm/temurin-21-jdk-amd64/bin/java 2100
                            update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/temurin-21-jdk-amd64/bin/javac 2100
                            update-alternatives --set java /usr/lib/jvm/temurin-21-jdk-amd64/bin/java
                            update-alternatives --set javac /usr/lib/jvm/temurin-21-jdk-amd64/bin/javac
                            
                            echo "Verificando instalação..."
                            ls -la /usr/lib/jvm/temurin-21-jdk-amd64/bin/
                            echo "JAVA_HOME atual: $JAVA_HOME"
                            export PATH="/usr/lib/jvm/temurin-21-jdk-amd64/bin:$PATH"
                        }
                        
                        # Função para instalar Java manualmente
                        install_java_manual() {
                            echo "Instalando Java 21 manualmente..."
                            cd /tmp
                            wget -q https://download.java.net/java/GA/jdk21.0.2/f2283984656d49d69e91c558476027ac/13/GPL/openjdk-21.0.2_linux-x64_bin.tar.gz
                            tar xzf openjdk-21.0.2_linux-x64_bin.tar.gz
                            rm -rf /usr/lib/jvm/temurin-21-jdk-amd64 || true
                            mkdir -p /usr/lib/jvm
                            mv jdk-21.0.2 /usr/lib/jvm/temurin-21-jdk-amd64
                            
                            echo "Configurando alternativas do Java..."
                            update-alternatives --install /usr/bin/java java /usr/lib/jvm/temurin-21-jdk-amd64/bin/java 2100
                            update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/temurin-21-jdk-amd64/bin/javac 2100
                            update-alternatives --set java /usr/lib/jvm/temurin-21-jdk-amd64/bin/java
                            update-alternatives --set javac /usr/lib/jvm/temurin-21-jdk-amd64/bin/javac
                            
                            export PATH="/usr/lib/jvm/temurin-21-jdk-amd64/bin:$PATH"
                            export JAVA_HOME="/usr/lib/jvm/temurin-21-jdk-amd64"
                        }
                        
                        echo "Estado inicial do Java:"
                        java -version || echo "Java não instalado inicialmente"
                        
                        if check_java_version; then
                            echo "Java 21 já está instalado e configurado corretamente"
                        else
                            echo "Iniciando instalação do Java 21..."
                            if command -v apt-get &> /dev/null; then
                                install_java_debian || install_java_manual
                            else
                                install_java_manual
                            fi
                            
                            echo "Verificando instalação final..."
                            if ! check_java_version; then
                                echo "ERRO: Falha na instalação do Java 21"
                                echo "PATH atual: $PATH"
                                echo "JAVA_HOME atual: $JAVA_HOME"
                                echo "Conteúdo de /usr/lib/jvm:"
                                ls -la /usr/lib/jvm/
                                echo "Alternativas do Java:"
                                update-alternatives --display java
                                exit 1
                            fi
                        fi
                        
                        echo "Configuração do Java concluída:"
                        echo "JAVA_HOME: $JAVA_HOME"
                        echo "PATH: $PATH"
                        java -version
                        javac -version
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

        stage('Wait for Application') {
            steps {
                script {
                        sh '''
                        echo "Verificando se a aplicação está respondendo..."
                        for i in {1..30}; do
                            if curl -s http://localhost:${APP_PORT}/actuator/health | grep -q "UP"; then
                                echo "Aplicação está saudável!"
                                exit 0
                            fi
                            echo "Aguardando aplicação inicializar... Tentativa $i/30"
                                sleep 10
                            done
                            
                            echo "Timeout aguardando aplicação inicializar"
                            exit 1
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
