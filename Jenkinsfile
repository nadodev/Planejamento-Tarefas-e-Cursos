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
        // Configura caminho alternativo para docker-compose
        COMPOSE_PATH = "/usr/local/bin/docker-compose"
    }

    stages {
        stage('Setup Environment') {
            steps {
                sh '''
                    # Instala docker-compose se não existir
                    if ! command -v docker-compose &> /dev/null; then
                        echo "Instalando docker-compose..."
                        curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" \
                        -o ${COMPOSE_PATH}
                        chmod +x ${COMPOSE_PATH}
                    fi
                    ${COMPOSE_PATH} --version
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
                    ${COMPOSE_PATH} down || true
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
            // Comente a linha abaixo se não tiver o plugin Slack instalado
            // slackSend(color: 'good', message: "Build ${DOCKER_IMAGE}:${DOCKER_TAG} sucedida")
        }
        failure {
            echo 'Pipeline falhou! Verifique os logs.'
            // Comente a linha abaixo se não tiver o plugin Slack instalado
            // slackSend(color: 'danger', message: "Build ${JOB_NAME} falhou")
        }
    }
}