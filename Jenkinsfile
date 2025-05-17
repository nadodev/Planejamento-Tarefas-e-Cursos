pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
        DB_HOST = 'db'
        DB_PORT = '3306'
    }

    stages {
        stage('Check Tools') {
            steps {
                script {
                    // Verifica se o Docker está instalado
                    sh '''
                        if ! command -v docker &> /dev/null; then
                            echo "Docker não está instalado!"
                            exit 1
                        fi
                        echo "Docker version:"
                        docker --version
                    '''

                    // Verifica se o Docker Compose está instalado
                    sh '''
                        if command -v docker-compose &> /dev/null; then
                            DOCKER_COMPOSE="docker-compose"
                        elif docker compose version &> /dev/null; then
                            DOCKER_COMPOSE="docker compose"
                        else
                            echo "Docker Compose não está instalado!"
                            exit 1
                        fi
                        echo "Docker Compose está disponível"
                    '''
                }
            }
        }

        stage('Start Containers') {
            steps {
                script {
                    // Tenta usar docker-compose primeiro, se falhar usa docker compose
                    sh '''
                        if command -v docker-compose &> /dev/null; then
                            echo "Usando docker-compose..."
                            docker-compose -f ${COMPOSE_FILE} up -d
                        else
                            echo "Usando docker compose..."
                            docker compose -f ${COMPOSE_FILE} up -d
                        fi
                    '''
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
                            if docker exec $(docker ps -qf "name=db") mysqladmin ping -h"${DB_HOST}" --silent &> /dev/null; then
                                echo "MySQL está pronto!"
                                break
                            fi
                            if [ $i -eq 30 ]; then
                                echo "Timeout esperando MySQL"
                                exit 1
                            fi
                            echo "Aguardando banco... tentativa $i/30"
                            sleep 3
                        done
                    '''
                }
            }
        }

        stage('Build & Test') {
            steps {
                sh '''
                    if [ -f "./mvnw" ]; then
                        chmod +x ./mvnw
                        ./mvnw clean verify
                    elif [ -f "./gradlew" ]; then
                        chmod +x ./gradlew
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
            echo "Encerrando containers (se necessário)..."
            sh '''
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
                docker logs $(docker ps -qf "name=db") || true
            '''
        }
    }
}
