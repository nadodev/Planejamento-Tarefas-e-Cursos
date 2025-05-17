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
                    # Para e remove o container MySQL existente se houver
                    docker stop mysql-planejador || true
                    docker rm mysql-planejador || true
                    
                    # Inicia o container MySQL
                    docker run -d \
                        --name mysql-planejador \
                        -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                        -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                        --network planejador-network \
                        mysql:8.0
                        
                    # Aguarda o MySQL iniciar
                    echo "Aguardando MySQL inicializar..."
                    
                    # Loop para verificar se o MySQL está pronto
                    for i in $(seq 1 30); do
                        if docker exec mysql-planejador mysqladmin ping -h localhost -u root -p${MYSQL_ROOT_PASSWORD} --silent; then
                            echo "MySQL está pronto!"
                            break
                        fi
                        echo "Tentativa $i: MySQL ainda não está pronto..."
                        sleep 10
                    done
                    
                    # Verifica se o banco foi criado
                    echo "Verificando banco de dados..."
                    docker exec mysql-planejador mysql -u root -p${MYSQL_ROOT_PASSWORD} -e "SHOW DATABASES;" | grep ${MYSQL_DATABASE}
                    
                    # Mostra informações do container
                    echo "Status do container MySQL:"
                    docker ps -f name=mysql-planejador
                    
                    # Mostra logs do MySQL
                    echo "Logs do MySQL:"
                    docker logs mysql-planejador
                '''
            }
        }

        stage('Deploy Application') {
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
                        -e SPRING_DATASOURCE_URL="jdbc:mysql://${DB_HOST}:${DB_PORT}/${MYSQL_DATABASE}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true" \
                        -e SPRING_DATASOURCE_USERNAME=root \
                        -e SPRING_DATASOURCE_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                        ${DOCKER_IMAGE}:${DOCKER_TAG}
                        
                    echo "Container da aplicação iniciado. Aguardando inicialização..."
                    sleep 10
                    
                    echo "Logs iniciais da aplicação:"
                    docker logs ${DOCKER_IMAGE}
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
                        echo "Verificando logs do MySQL..."
                        sh "docker logs mysql-planejador"
                        
                        echo "Verificando logs da aplicação..."
                        sh "docker logs ${DOCKER_IMAGE}"
                        
                        echo "Verificando conectividade entre containers..."
                        sh '''
                            docker exec ${DOCKER_IMAGE} ping -c 3 mysql-planejador || true
                            
                            echo "Testando conexão MySQL diretamente..."
                            docker exec ${DOCKER_IMAGE} java -jar /app/target/planejador_horario-0.0.1-SNAPSHOT.jar \
                                --spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${MYSQL_DATABASE} \
                                --spring.datasource.username=root \
                                --spring.datasource.password=${MYSQL_ROOT_PASSWORD} \
                                --spring.main.web-application-type=none \
                                --logging.level.root=DEBUG
                        '''
                        
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
            sh '''
                echo "=== Logs do MySQL ==="
                docker logs mysql-planejador || true
                
                echo "=== Logs da Aplicação ==="
                docker logs ${DOCKER_IMAGE} || true
                
                echo "=== Status dos Containers ==="
                docker ps -a
                
                echo "=== Informações da Rede ==="
                docker network inspect planejador-network
                
                echo "=== Teste de DNS ==="
                docker exec ${DOCKER_IMAGE} nslookup mysql-planejador || true
                
                echo "=== Configuração de Rede do Container ==="
                docker exec ${DOCKER_IMAGE} ip addr show || true
            '''
        }
    }
}
