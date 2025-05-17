pipeline {
    agent any

    environment {
        // Defina explicitamente o JAVA_HOME
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk-amd64/bin/java' // Ajuste o caminho conforme sua instalação
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        DOCKER_IMAGE = 'planejador-horario'
        DOCKER_TAG = "v${BUILD_NUMBER}"
    }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            steps {
                sh '''
                    # Garante permissões de execução
                    chmod +x mvnw
                    
                    # Executa o build com o Java 21
                    export JAVA_HOME=${JAVA_HOME}
                    ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('Testes Unitários') {
            steps {
                sh './mvnw test'
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
                    docker-compose down || true
                    docker-compose up -d
                '''
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        failure {
            echo 'Pipeline falhou! Verifique os logs para detalhes.'
        }
    }
}