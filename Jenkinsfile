pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
        DB_HOST = 'db'
        DB_PORT = '3306'
    }

    stages {
        stage('Start Containers') {
            steps {
                script {
                    // Sobe containers em segundo plano
                    sh 'docker-compose up -d'
                }
            }
        }

        stage('Wait for MySQL') {
            steps {
                script {
                    // Aguarda o MySQL estar disponível antes de seguir
                    sh '''
                        echo "Esperando o MySQL iniciar..."
                        for i in {1..30}; do
                            docker exec $(docker ps -qf "ancestor=mysql:8.0") mysqladmin ping -h"${DB_HOST}" -P${DB_PORT} --silent && break
                            echo "Aguardando banco..."
                            sleep 3
                        done
                    '''
                }
            }
        }

        stage('Build & Test') {
            steps {
                sh './mvnw clean verify' // ou ./gradlew se usar Gradle
            }
        }

        stage('Stop Containers') {
            steps {
                script {
                    sh 'docker-compose down -v'
                }
            }
        }
    }

    post {
        always {
            echo "Encerrando containers (se necessário)..."
            sh 'docker-compose down -v || true'
        }
    }
}
