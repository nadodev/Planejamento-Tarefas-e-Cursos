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
    }

    stages {
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
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline executado com sucesso!'
            slackSend(color: 'good', message: "Build ${DOCKER_IMAGE}:${DOCKER_TAG} sucedida")
        }
        failure {
            echo 'Pipeline falhou! Verifique os logs.'
            slackSend(color: 'danger', message: "Build ${JOB_NAME} falhou")
        }
    }
}