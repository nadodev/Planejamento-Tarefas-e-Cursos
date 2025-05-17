pipeline {
    agent any

    tools {
        jdk 'JDK 21' // Nome cadastrado no Jenkins em "Manage Jenkins → Global Tool Configuration"
    }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk-amd64' // Resolve dinamicamente o path
        DOCKER_IMAGE = 'planejador-horario'
        DOCKER_TAG = "v${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
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
                    docker-compose down
                    docker-compose up -d
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
            echo 'Pipeline falhou!'
        }
    }
}
