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
                script {
                    // Verifica se o Docker está funcionando
                    sh 'docker info'
                    
                    echo "Limpando ambiente anterior..."
                    
                    // Lista containers antes da limpeza
                    sh 'echo "Containers antes da limpeza:" && docker ps -a'
                    
                    // Remove containers antigos de forma mais segura
                    sh '''
                        for container in planejador-horario mysql-planejador; do
                            if docker ps -a --format '{{.Names}}' | grep -q "^${container}\$"; then
                                echo "Removendo container ${container}..."
                                docker stop ${container} || true
                                docker rm -f ${container} || true
                            else
                                echo "Container ${container} não encontrado"
                            fi
                        done
                    '''
                    
                    // Lista containers após a limpeza
                    sh 'echo "Containers após limpeza:" && docker ps -a'
                    
                    // Verifica e cria a rede
                    sh '''
                        if ! docker network inspect planejador-network >/dev/null 2>&1; then
                            echo "Criando rede planejador-network..."
                            docker network create planejador-network
                        else
                            echo "Rede planejador-network já existe"
                            echo "Containers na rede:"
                            docker network inspect planejador-network
                        fi
                    '''
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
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    sh "docker images | grep ${DOCKER_IMAGE}"
                }
            }
        }

        stage('Deploy Database') {
            steps {
                script {
                    echo "Iniciando MySQL..."
                    
                    sh """
                        # Inicia o container MySQL
                        docker run -d \
                            --name mysql-planejador \
                            -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                            -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                            --network planejador-network \
                            mysql:8.0
                            
                        echo "Container MySQL iniciado. Status:"
                        docker ps -a | grep mysql-planejador
                        
                        echo "Aguardando MySQL inicializar..."
                        
                        # Loop para verificar se o MySQL está pronto
                        for i in \$(seq 1 30); do
                            if docker exec mysql-planejador mysqladmin ping -h localhost -u root -p${MYSQL_ROOT_PASSWORD} --silent; then
                                echo "MySQL está respondendo!"
                                
                                if docker exec mysql-planejador mysql -u root -p${MYSQL_ROOT_PASSWORD} -e "USE ${MYSQL_DATABASE}"; then
                                    echo "Banco de dados ${MYSQL_DATABASE} está acessível"
                                    break
                                fi
                            fi
                            
                            if [ \$i -eq 30 ]; then
                                echo "Timeout aguardando MySQL. Logs:"
                                docker logs mysql-planejador
                                exit 1
                            fi
                            
                            echo "Tentativa \$i: MySQL ainda não está pronto..."
                            sleep 10
                        done
                    """
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    echo "Iniciando aplicação..."
                    
                    sh """
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
                            
                        echo "Container da aplicação iniciado. Status:"
                        docker ps -a | grep ${DOCKER_IMAGE}
                        
                        echo "Aguardando inicialização..."
                        sleep 10
                        
                        echo "Logs da aplicação:"
                        docker logs ${DOCKER_IMAGE}
                        
                        echo "Verificando conectividade..."
                        docker exec ${DOCKER_IMAGE} ping -c 3 mysql-planejador || true
                        docker exec ${DOCKER_IMAGE} nc -zv mysql-planejador 3306 || true
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
            script {
                echo 'Pipeline falhou!'
                
                sh '''
                    echo "=== Status dos Containers ==="
                    docker ps -a
                    
                    echo "=== Informações da Rede ==="
                    docker network inspect planejador-network
                    
                    echo "=== Logs do MySQL ==="
                    docker logs mysql-planejador || echo "MySQL container não encontrado"
                    
                    echo "=== Logs da Aplicação ==="
                    docker logs planejador-horario || echo "Container da aplicação não encontrado"
                    
                    echo "=== Tentando diagnóstico de rede ==="
                    docker exec planejador-horario ping -c 3 mysql-planejador || echo "Não foi possível executar ping"
                    docker exec planejador-horario nc -zv mysql-planejador 3306 || echo "Não foi possível verificar porta"
                '''
            }
        }
    }
}
