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
                    try {
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
                        
                        // Remove a rede se existir e recria
                        sh '''
                            echo "Recriando rede Docker..."
                            docker network rm planejador-network || true
                            docker network create planejador-network
                            echo "Rede criada:"
                            docker network inspect planejador-network
                        '''
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
                            # Usando modo legado do Docker build para melhor compatibilidade
                            docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                            
                            echo "\\n=== Imagem construída ==="
                            docker images | grep ${DOCKER_IMAGE}
                            
                            echo "\\n=== Testando ferramentas instaladas ==="
                            # Testa cada ferramenta individualmente para melhor diagnóstico
                            echo "Verificando ping..."
                            docker run --rm ${DOCKER_IMAGE}:${DOCKER_TAG} which ping
                            
                            echo "Verificando netcat..."
                            docker run --rm ${DOCKER_IMAGE}:${DOCKER_TAG} which nc
                            
                            echo "Verificando dig..."
                            docker run --rm ${DOCKER_IMAGE}:${DOCKER_TAG} which dig
                            
                            echo "Verificando curl..."
                            docker run --rm ${DOCKER_IMAGE}:${DOCKER_TAG} which curl
                            
                            echo "\\n=== Verificando conteúdo do container ==="
                            docker run --rm ${DOCKER_IMAGE}:${DOCKER_TAG} ls -la /app/target/
                            docker run --rm ${DOCKER_IMAGE}:${DOCKER_TAG} ls -la /app/healthcheck.sh
                        """
                    } catch (Exception e) {
                        echo "Erro ao construir imagem Docker: ${e.message}"
                        currentBuild.result = 'FAILURE'
                        error "Falha ao construir imagem Docker"
                    }
                }
            }
        }

        stage('Deploy Database') {
            steps {
                script {
                    try {
                        echo "Iniciando MySQL..."
                        
                        sh '''
                            # Inicia o container MySQL com configurações otimizadas
                            echo "Criando container MySQL..."
                            docker run -d \
                                --name mysql-planejador \
                                -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                                -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                                -e MYSQL_ROOT_HOST='%' \
                                -e MYSQL_USER=app_user \
                                -e MYSQL_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                                --network planejador-network \
                                -p 3306:3306 \
                                mysql:8.0 \
                                --character-set-server=utf8mb4 \
                                --collation-server=utf8mb4_unicode_ci \
                                --default-authentication-plugin=mysql_native_password \
                                --bind-address=0.0.0.0 \
                                --max_connections=1000
                        '''
                        
                        # Verifica se o container foi criado
                        if ! docker ps -a | grep -q mysql-planejador; then
                            echo "Falha ao criar container MySQL"
                            exit 1
                        fi
                        
                        echo "Container MySQL criado. Status:"
                        docker ps -a | grep mysql-planejador
                        
                        echo "Aguardando MySQL inicializar..."
                        
                        # Loop para verificar se o MySQL está pronto
                        for i in \$(seq 1 30); do
                            echo "Tentativa \$i: Verificando MySQL..."
                            
                            if docker exec mysql-planejador mysqladmin ping -h localhost -u root -p${MYSQL_ROOT_PASSWORD} --silent; then
                                echo "MySQL está respondendo!"
                                
                                # Configura permissões do MySQL
                                echo "Configurando permissões do MySQL..."
                                docker exec mysql-planejador mysql -u root -p${MYSQL_ROOT_PASSWORD} -e "ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '${MYSQL_ROOT_PASSWORD}'; FLUSH PRIVILEGES;"
                                
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
                            
                            echo "MySQL ainda não está pronto. Aguardando..."
                            sleep 10
                        done
                        
                        # Testa conexão do MySQL
                        echo "Testando conexão MySQL de fora do container..."
                        docker run --rm --network planejador-network mysql:8.0 mysql -h mysql-planejador -u root -p${MYSQL_ROOT_PASSWORD} -e "SELECT 1;"
                    } catch (Exception e) {
                        echo "Erro ao iniciar MySQL: ${e.message}"
                        sh 'docker logs mysql-planejador || true'
                        throw e
                    }
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    try {
                        echo "Iniciando aplicação..."
                        
                        sh """
                            echo "Criando container da aplicação..."
                            docker run -d \
                                --name ${DOCKER_IMAGE} \
                                -p 8080:8080 \
                                --network planejador-network \
                                -e SPRING_DATASOURCE_URL="jdbc:mysql://${DB_HOST}:${DB_PORT}/${MYSQL_DATABASE}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true" \
                                -e SPRING_DATASOURCE_USERNAME=root \
                                -e SPRING_DATASOURCE_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                                -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                                -e SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect \
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
                            
                            sh '''
                                # Teste de DNS
                                echo "Teste de resolução DNS..."
                                docker exec ${DOCKER_IMAGE} dig mysql-planejador
                                
                                # Teste de ping
                                echo "Teste de ping..."
                                docker exec ${DOCKER_IMAGE} ping -c 3 mysql-planejador
                                
                                # Teste de porta MySQL
                                echo "Teste de conexão MySQL..."
                                docker exec ${DOCKER_IMAGE} nc -zv mysql-planejador 3306
                                
                                # Teste de conexão MySQL via cliente
                                echo "Teste de cliente MySQL..."
                                docker exec mysql-planejador mysql -h localhost -u root -p${MYSQL_ROOT_PASSWORD} -e "SELECT VERSION();"
                                
                                # Verifica status da rede
                                echo "Verificando configuração de rede..."
                                docker network inspect planejador-network
                            '''
                            
                            echo "Testando conexão MySQL da aplicação..."
                            docker exec ${DOCKER_IMAGE} java -jar /app/target/planejador_horario-0.0.1-SNAPSHOT.jar \
                                --spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${MYSQL_DATABASE} \
                                --spring.datasource.username=root \
                                --spring.datasource.password=${MYSQL_ROOT_PASSWORD} \
                                --spring.main.web-application-type=none \
                                --logging.level.root=DEBUG \
                                --logging.level.org.hibernate=DEBUG \
                                --spring.jpa.properties.hibernate.show_sql=true
                            
                            echo "Verificando status da aplicação..."
                            for i in \$(seq 1 12); do
                                if curl -s http://localhost:8080/actuator/health | grep -q "UP"; then
                                    echo "Aplicação está saudável!"
                                    exit 0
                                fi
                                echo "Aguardando aplicação inicializar... Tentativa \$i"
                                sleep 10
                            done
                            
                            echo "Timeout aguardando aplicação inicializar"
                            docker logs ${DOCKER_IMAGE}
                            exit 1
                        """
                    } catch (Exception e) {
                        echo "Erro ao iniciar aplicação: ${e.message}"
                        sh """
                            echo "=== Logs da Aplicação ==="
                            docker logs ${DOCKER_IMAGE} || true
                            
                            echo "=== Status dos Containers ==="
                            docker ps -a
                            
                            echo "=== Informações da Rede ==="
                            docker network inspect planejador-network
                            
                            echo "=== Teste de conexão MySQL ==="
                            docker run --rm --network planejador-network mysql:8.0 mysql -h mysql-planejador -u root -p${MYSQL_ROOT_PASSWORD} -e "SELECT 1;" || true
                        """
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
                    docker network inspect planejador-network
                    
                    echo "=== Logs Finais do MySQL ==="
                    docker logs mysql-planejador || echo "MySQL container não encontrado"
                    
                    echo "=== Logs Finais da Aplicação ==="
                    docker logs planejador-horario || echo "Container da aplicação não encontrado"
                    
                    echo "=== Teste Final de Conexão MySQL ==="
                    docker run --rm --network planejador-network mysql:8.0 mysql -h mysql-planejador -u root -p${MYSQL_ROOT_PASSWORD} -e "SELECT 1;" || echo "Não foi possível conectar ao MySQL"
                '''
            }
        }
    }
}
