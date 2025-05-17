pipeline {
    agent {
        docker {
            image 'cimg/openjdk:21.0'
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
        stage('Setup') {
            steps {
                sh '''
                    # Verifica se o Docker está funcionando
                    docker info
                    
                    # Cria a rede se não existir
                    docker network inspect planejador-network >/dev/null 2>&1 || \
                    docker network create planejador-network
                '''
            }
        }

        stage('Checkout') {
            steps {
                cleanWs()
                git branch: env.BRANCH, url: env.GITHUB_REPO
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    # Para e remove o container existente se houver
                    docker stop ${DOCKER_IMAGE} || true
                    docker rm ${DOCKER_IMAGE} || true
                    
                    # Executa o novo container
                    docker run -d \
                        --name ${DOCKER_IMAGE} \
                        -p 8080:8080 \
                        --network planejador-network \
                        ${DOCKER_IMAGE}:${DOCKER_TAG}
                '''
            }
        }

        stage('Health Check') {
            steps {
                script {
                    // Aguarda a inicialização
                    sh 'sleep 30'

                    // Verifica status do container
                    def status = sh(
                        script: "docker ps -f name=${DOCKER_IMAGE} --format '{{.Status}}'",
                        returnStdout: true
                    ).trim()

                    if (!status.startsWith('Up')) {
                        sh "docker logs ${DOCKER_IMAGE}"
                        error "Container não está rodando. Status: ${status}"
                    }

                    // Verifica saúde da aplicação
                    def health = sh(
                        script: 'curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health || echo "000"',
                        returnStdout: true
                    ).trim()

                    if (health != "200") {
                        sh "docker logs ${DOCKER_IMAGE}"
                        error "Aplicação não está saudável. HTTP Status: ${health}"
                    }

                    echo "Aplicação está rodando e saudável!"
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
            sh "docker logs ${DOCKER_IMAGE} || true"
        }
    }
}
