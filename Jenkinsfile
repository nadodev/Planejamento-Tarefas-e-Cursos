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
        MYSQL_ROOT_PASSWORD = '1234'
        MYSQL_DATABASE = 'tempomente'
        DB_HOST = 'mysql'
        DB_PORT = '3306'
        DOCKER_NETWORK = 'planejador-horario_default'
    }

    stages {
        stage('Setup') {
            steps {
                script {
                    try {
                        // Verifica se o Docker está funcionando
                        sh 'docker info'
                        
                        echo "Limpando ambiente anterior..."
                        
                        // Lista containers antes da limpeza
                        sh 'echo "Containers antes da limpeza:" && docker ps -a'
                        
                        // Remove apenas o container da aplicação se existir
                        sh '''
                            if docker ps -a --format '{{.Names}}' | grep -q "^${DOCKER_IMAGE}\$"; then
                                echo "Removendo container ${DOCKER_IMAGE}..."
                                docker stop ${DOCKER_IMAGE} || true
                                docker rm -f ${DOCKER_IMAGE} || true
                            else
                                echo "Container ${DOCKER_IMAGE} não encontrado"
                            fi
                        '''
                        
                        // Lista containers após a limpeza
                        sh 'echo "Containers após limpeza:" && docker ps -a'
                        
                    } catch (Exception e) {
                        echo "Erro durante o setup: ${e.message}"
                        throw e
                    }
                }
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
                    try {
                        echo "Iniciando build da imagem Docker..."
                        
                        sh """
                            # Mostra conteúdo do Dockerfile
                            echo "=== Dockerfile ===="
                            cat Dockerfile
                            
                            echo "\\n=== Iniciando build ==="
                            docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                            
                            echo "\\n=== Imagem construída ==="
                            docker images | grep ${DOCKER_IMAGE}
                        """
                    } catch (Exception e) {
                        echo "Erro ao construir imagem Docker: ${e.message}"
                        currentBuild.result = 'FAILURE'
                        error "Falha ao construir imagem Docker"
                    }
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    try {
                        echo "Iniciando aplicação..."
                        
                        sh '''
                            # Verifica se a rede do docker-compose existe
                            if ! docker network ls | grep -q ${DOCKER_NETWORK}; then
                                echo "ERRO: Rede ${DOCKER_NETWORK} não encontrada. Verifique se o docker-compose está rodando."
                                exit 1
                            fi

                            # Verifica se o MySQL está acessível
                            echo "Verificando conexão com MySQL..."
                            docker run --rm --network ${DOCKER_NETWORK} mysql:8.0 mysql -h${DB_HOST} -uroot -p${MYSQL_ROOT_PASSWORD} -e "SELECT 1;" || {
                                echo "ERRO: Não foi possível conectar ao MySQL. Verifique se o container está rodando."
                                exit 1
                            }
                            
                            echo "Criando container da aplicação..."
                            docker run -d \
                                --name ${DOCKER_IMAGE} \
                                -p 8080:8080 \
                                --network ${DOCKER_NETWORK} \
                                -e SPRING_DATASOURCE_URL="jdbc:mysql://${DB_HOST}:${DB_PORT}/${MYSQL_DATABASE}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true&autoReconnect=true&failOverReadOnly=false&maxReconnects=10" \
                                -e SPRING_DATASOURCE_USERNAME=root \
                                -e SPRING_DATASOURCE_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                                -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                                -e SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect \
                                -e SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=10 \
                                -e SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=5 \
                                -e SPRING_DATASOURCE_HIKARI_CONNECTION_TIMEOUT=30000 \
                                -e SPRING_DATASOURCE_HIKARI_IDLE_TIMEOUT=300000 \
                                -e SPRING_DATASOURCE_HIKARI_MAX_LIFETIME=1200000 \
                                -e LOGGING_LEVEL_ROOT=DEBUG \
                                -e LOGGING_LEVEL_ORG_SPRINGFRAMEWORK=DEBUG \
                                -e LOGGING_LEVEL_ORG_HIBERNATE=DEBUG \
                                ${DOCKER_IMAGE}:${DOCKER_TAG}

                            # Verifica se o container foi criado
                            if ! docker ps -a | grep -q ${DOCKER_IMAGE}; then
                                echo "Falha ao criar container da aplicação"
                                exit 1
                            fi
                            
                            echo "Container da aplicação criado. Status:"
                            docker ps -a | grep ${DOCKER_IMAGE}
                            
                            echo "Aguardando inicialização..."
                            sleep 10
                            
                            echo "Logs da aplicação:"
                            docker logs ${DOCKER_IMAGE}
                            
                            echo "Verificando conectividade..."
                            
                            # Teste de DNS
                            echo "Teste de resolução DNS..."
                            docker exec ${DOCKER_IMAGE} dig ${DB_HOST}
                            
                            # Teste de porta MySQL
                            echo "Teste de conexão MySQL..."
                            docker exec ${DOCKER_IMAGE} nc -zv ${DB_HOST} 3306
                            
                            echo "Verificando status da aplicação..."
                            for i in $(seq 1 12); do
                                if curl -s http://localhost:8080/actuator/health | grep -q "UP"; then
                                    echo "Aplicação está saudável!"
                                    exit 0
                                fi
                                echo "Aguardando aplicação inicializar... Tentativa $i"
                                sleep 10
                            done
                            
                            echo "Timeout aguardando aplicação inicializar"
                            docker logs ${DOCKER_IMAGE}
                            exit 1
                        '''
                    } catch (Exception e) {
                        echo "Erro ao iniciar aplicação: ${e.message}"
                        sh '''
                            echo "=== Logs da Aplicação ==="
                            docker logs ${DOCKER_IMAGE} || true
                            
                            echo "=== Status dos Containers ==="
                            docker ps -a
                            
                            echo "=== Informações da Rede ==="
                            docker network inspect ${DOCKER_NETWORK} || true
                        '''
                        throw e
                    }
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
            script {
                echo 'Pipeline falhou!'
                
                sh '''
                    echo "=== Status Final dos Containers ==="
                    docker ps -a
                    
                    echo "=== Informações Finais da Rede ==="
                    docker network inspect ${DOCKER_NETWORK} || true
                    
                    echo "=== Logs Finais da Aplicação ==="
                    docker logs ${DOCKER_IMAGE} || echo "Container da aplicação não encontrado"
                '''
            }
        }
    }
}
