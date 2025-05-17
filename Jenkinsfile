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
        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        
        stage('Docker Build') {
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
        
        stage('Verification') {
            steps {
                script {
                    // Aguarda inicialização
                    sleep 30
                    
                    // Verifica status do container
                    def status = sh(
                        script: "${COMPOSE_PATH} ps | grep java-app-1 | awk '{print \$4}'",
                        returnStdout: true
                    ).trim()
                    
                    if (status != "Up") {
                        error "Container não está rodando. Status: ${status}"
                        sh 'docker logs java-app-1'
                    }
                    
                    // Testa conexão
                    def response = sh(
                        script: 'curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health || echo "000"',
                        returnStdout: true
                    ).trim()
                    
                    if (response != "200") {
                        error "Aplicação não responde. HTTP Status: ${response}"
                        sh 'docker logs java-app-1'
                    } else {
                        echo "SUCESSO: Aplicação respondendo na porta 8080!"
                    }
                }
            }
        }
    }
}