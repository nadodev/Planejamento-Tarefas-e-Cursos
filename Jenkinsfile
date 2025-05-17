pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'planejador-horario'
        DOCKER_TAG = "v${BUILD_NUMBER}"
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk-amd64'
    }

    tools {
        jdk 'JDK 21' // Certifique-se de que 'JDK 17' está cadastrado no Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            steps {
                script {
                    def javaHome = tool(name: 'JDK 21', type: 'jdk')
                    env.JAVA_HOME = javaHome
                    env.PATH = "${javaHome}/bin:${env.PATH}"
                }
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
                script {
                    sh """
                        docker-compose down
                        docker-compose up -d
                    """
                }
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
