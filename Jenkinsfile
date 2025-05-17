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
                    # Instala dependências ausentes
                    apt-get update && apt-get install -y git docker-compose
                    
                    # Verifica instalações
                    git --version
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
                    
                    # Verifica status dos containers
                    sleep 10
                    ${COMPOSE_PATH} ps
                '''
            }
        }
    }
    
    post {
        always {
            cleanWs()
            script {
                // Notificação opcional (descomente se configurado)
                // slackSend(color: currentBuild.result == 'SUCCESS' ? 'good' : 'danger',
                //           message: "Build ${env.JOB_NAME} #${env.BUILD_NUMBER}: ${currentBuild.result}")
            }
        }
    }
}