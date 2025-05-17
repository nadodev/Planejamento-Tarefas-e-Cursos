pipeline {
    agent {
        docker {
            image 'eclipse-temurin:21-jdk'
            args '-v /var/run/docker.sock:/var/run/docker.sock -v $HOME/.m2:/root/.m2'
        }
    }

    environment {
        DOCKER_IMAGE = 'planejador-horario'
        DOCKER_TAG = "v${BUILD_NUMBER}"
        GITHUB_REPO = 'https://github.com/nadodev/Planejamento-Tarefas-e-Cursos.git'
        BRANCH = 'developer'
        SPRING_PROFILES_ACTIVE = 'test'
    }

    stages {
        stage('Checkout') {
            steps {
                // Limpa workspace
                cleanWs()
                // Clone do repositório
                git branch: env.BRANCH, url: env.GITHUB_REPO
            }
        }

        stage('Build') {
            steps {
                // Garante permissão de execução do Maven wrapper
                sh 'chmod +x mvnw'
                
                // Compila e executa testes
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Constrói a imagem Docker
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Para containers existentes e inicia novos
                    sh '''
                        docker-compose down || true
                        docker-compose up -d --build
                    '''
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    // Aguarda a inicialização
                    sleep 30

                    // Verifica se o container está rodando
                    def containerStatus = sh(
                        script: "docker ps --filter name=planejador-horario --format '{{.Status}}'",
                        returnStdout: true
                    ).trim()

                    if (!containerStatus.startsWith('Up')) {
                        error "Container não está rodando. Status: ${containerStatus}"
                    }

                    // Tenta acessar a aplicação
                    def healthCheck = sh(
                        script: 'curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health || echo "000"',
                        returnStdout: true
                    ).trim()

                    if (healthCheck != "200") {
                        error "Aplicação não está saudável. Status HTTP: ${healthCheck}"
                    }

                    echo "Aplicação está rodando e saudável!"
                }
            }
        }
    }

    post {
        always {
            // Limpa workspace
            cleanWs()
        }
        success {
            echo 'Pipeline executado com sucesso!'
        }
        failure {
            echo 'Pipeline falhou!'
            // Coleta logs em caso de falha
            sh 'docker logs planejador-horario || true'
        }
    }
}
