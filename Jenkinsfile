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
        DB_HOST = 'mysql-planejador'
        DB_PORT = '3306'
    }

    stages {
        stage('Setup') {
            steps {
                sh '''
                    # Verifica se o Docker está funcionando
                    docker info
                    
                    # Remove containers antigos se existirem
                    docker rm -f $(docker ps -aq -f name=planejador-horario) || true
                    docker rm -f $(docker ps -aq -f name=mysql-planejador) || true
                    
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

        stage('Deploy Database') {
            steps {
                sh '''
                    # Inicia o container MySQL
                    docker run -d \
                        --name mysql-planejador \
                        -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                        -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                        --network planejador-network \
                        mysql:8.0
                        
                    echo "Aguardando MySQL inicializar..."
                    
                    # Loop para verificar se o MySQL está pronto
                    for i in $(seq 1 30); do
                        if docker exec mysql-planejador mysqladmin ping -h localhost -u root -p${MYSQL_ROOT_PASSWORD} --silent; then
                            echo "MySQL está pronto!"
                            
                            # Verifica se o banco foi criado
                            if docker exec mysql-planejador mysql -u root -p${MYSQL_ROOT_PASSWORD} -e "USE ${MYSQL_DATABASE}"; then
                                echo "Banco de dados ${MYSQL_DATABASE} está acessível"
                                break
                            fi
                        fi
                        
                        if [ $i -eq 30 ]; then
                            echo "Timeout aguardando MySQL"
                            docker logs mysql-planejador
                            exit 1
                        fi
                        
                        echo "Tentativa $i: MySQL ainda não está pronto..."
                        sleep 10
                    done
                '''
            }
        }

        stage('Deploy Application') {
            steps {
                sh '''
                    # Executa o novo container
                    docker run -d \
                        --name ${DOCKER_IMAGE} \
                        -p 8080:8080 \
                        --network planejador-network \
                        -e SPRING_DATASOURCE_URL="jdbc:mysql://${DB_HOST}:${DB_PORT}/${MYSQL_DATABASE}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true" \
                        -e SPRING_DATASOURCE_USERNAME=root \
                        -e SPRING_DATASOURCE_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                        -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                        -e LOGGING_LEVEL_ROOT=DEBUG \
                        -e LOGGING_LEVEL_ORG_SPRINGFRAMEWORK=DEBUG \
                        ${DOCKER_IMAGE}:${DOCKER_TAG}
                        
                    echo "Container da aplicação iniciado. Aguardando inicialização..."
                    
                    # Aguarda e verifica os logs
                    sleep 10
                    docker logs ${DOCKER_IMAGE}
                    
                    # Executa healthcheck
                    docker exec ${DOCKER_IMAGE} /app/healthcheck.sh
                '''
            }
        }

        stage('Health Check') {
            steps {
                script {
                    sh '''
                        # Verifica conectividade com MySQL
                        echo "Verificando conectividade com MySQL..."
                        docker exec ${DOCKER_IMAGE} ping -c 3 mysql-planejador
                        
                        echo "Verificando porta do MySQL..."
                        docker exec ${DOCKER_IMAGE} nc -zv mysql-planejador 3306
                        
                        echo "Verificando resolução DNS..."
                        docker exec ${DOCKER_IMAGE} dig mysql-planejador
                        
                        # Aguarda a aplicação inicializar
                        for i in $(seq 1 12); do
                            echo "Tentativa $i de verificar saúde da aplicação..."
                            
                            if curl -s http://localhost:8080/actuator/health | grep -q "UP"; then
                                echo "Aplicação está saudável!"
                                exit 0
                            fi
                            
                            echo "Aguardando 10 segundos..."
                            sleep 10
                            
                            if [ $i -eq 12 ]; then
                                echo "=== Logs da Aplicação ==="
                                docker logs ${DOCKER_IMAGE}
                                
                                echo "=== Logs do MySQL ==="
                                docker logs mysql-planejador
                                
                                echo "=== Status dos Containers ==="
                                docker ps -a
                                
                                echo "=== Informações da Rede ==="
                                docker network inspect planejador-network
                                
                                exit 1
                            fi
                        done
                    '''
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
            sh '''
                echo "=== Logs do MySQL ==="
                docker logs mysql-planejador || true
                
                echo "=== Logs da Aplicação ==="
                docker logs ${DOCKER_IMAGE} || true
                
                echo "=== Status dos Containers ==="
                docker ps -a
                
                echo "=== Informações da Rede ==="
                docker network inspect planejador-network
            '''
        }
    }
}
