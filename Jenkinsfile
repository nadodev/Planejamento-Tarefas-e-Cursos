pipeline {
    agent {
        docker {
            image 'eclipse-temurin:21-jdk'
            args '-v $HOME/.m2:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock'
            reuseNode true
        }
    }
    
    environment {
        DOCKER_IMAGE = 'planejador-horario'
        DOCKER_TAG = "v${BUILD_NUMBER}"
        COMPOSE_PATH = "/usr/local/bin/docker-compose"
    }

    stages {
        stage('Setup Environment') {
            steps {
                sh '''
                    # Instala docker-compose se necessário
                    if ! command -v docker-compose &> /dev/null; then
                        echo "Instalando docker-compose..."
                        curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" \
                        -o ${COMPOSE_PATH}
                        chmod +x ${COMPOSE_PATH}
                    fi
                    
                    # Verifica e libera a porta 3306 se estiver em uso
                    if docker ps --format '{{.Ports}}' | grep -q '3306/tcp'; then
                        echo "Parando containers MySQL existentes..."
                        docker stop $(docker ps -q --filter "publish=3306") || true
                    fi
                '''
            }
        }
        
        stage('Build') {
            steps {
                sh '''
                    echo "=== Ambiente de Build ==="
                    echo "Java version:"
                    java -version
                    echo "Maven version:"
                    ./mvnw --version
                    
                    ./mvnw clean package -DskipTests
                '''
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }
        
        stage('Deploy') {
            steps {
                sh '''
                    # Para containers existentes e remove
                    ${COMPOSE_PATH} down || true
                    
                    # Verifica se a porta 3306 está livre
                    while netstat -tuln | grep -q ':3306'; do
                        echo "Porta 3306 em uso, aguardando liberação..."
                        sleep 5
                    done
                    
                    # Inicia os containers
                    ${COMPOSE_PATH} up -d --build
                '''
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline executado com sucesso!'
        }
        failure {
            echo 'Pipeline falhou! Verifique os logs.'
        }
    }
}