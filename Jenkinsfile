pipeline {
    agent any

    tools {
        jdk 'jdk-21' // Certifique-se que está cadastrado em "Manage Jenkins → Global Tool Configuration"
        maven 'maven' // Adicione também a configuração do Maven nas ferramentas globais
    }

    environment {
        DOCKER_IMAGE = 'planejador-horario'
        DOCKER_TAG = "v${BUILD_NUMBER}"
        // Configurações adicionais para garantir o uso do Java 21
        MAVEN_OPTS = "-Xmx1024m -Djava.home=${tool 'jdk-21'}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Setup Environment') {
            steps {
                script {
                    // Configura explicitamente o ambiente Java
                    def javaHome = tool name: 'jdk-21', type: 'jdk'
                    env.JAVA_HOME = javaHome
                    env.PATH = "${javaHome}/bin:${env.PATH}"
                    
                    // Verificação do ambiente
                    sh '''
                        echo "Java version:"
                        java -version
                        echo "Maven version:"
                        ./mvnw --version
                    '''
                }
            }
        }

        stage('Build Maven') {
            steps {
                sh 'chmod +x mvnw' // Garante permissões de execução
                sh './mvnw clean package -DskipTests'
            }
            
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Testes Unitários') {
            steps {
                sh './mvnw test'
            }
            
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml' // Relatórios de teste
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build da imagem Docker
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}", '--build-arg JAR_FILE=target/*.jar .')
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Para containers existentes
                    sh 'docker-compose down || true'
                    
                    // Inicia os containers
                    sh "docker-compose up -d"
                    
                    // Limpeza de containers antigos
                    sh 'docker system prune -f'
                }
            }
        }
        stage('Debug Environment') {
            steps {
                sh '''
                    echo "=== Debug Information ==="
                    echo "Current Java:"
                    which java
                    java -version
                    echo "JAVA_HOME: $JAVA_HOME"
                    echo "Tool JDK-21 path:"
                    ls -la ${tool 'jdk-21'}
                    echo "Environment:"
                    env | sort
                '''
            }
        }
        
    }

    post {
        always {
            cleanWs()
            script {
                // Notificação do status do build
                if (currentBuild.result == 'SUCCESS') {
                    echo 'Pipeline executado com sucesso!'
                    // slackSend channel: '#dev', message: "Build ${BUILD_NUMBER} sucedido!"
                } else {
                    echo 'Pipeline falhou!'
                    // slackSend channel: '#dev', message: "Build ${BUILD_NUMBER} falhou!"
                }
            }
        }
    }
}