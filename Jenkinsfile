pipeline {
    agent any

    environment {
        // Defina explicitamente o JAVA_HOME
        JAVA_HOME = '   '  
        PATH = "${JAVA_HOME}/bin:${PATH}"
        DOCKER_IMAGE = 'planejador-horario'
        DOCKER_TAG = "v${BUILD_NUMBER}"
    }

    stages {
       
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            steps {
                sh '''
                    # Definir variáveis de ambiente corretamente
                    export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                    export PATH="$JAVA_HOME/bin:$PATH"
                    
                    # Forçar atualização do alternatives
                    sudo update-alternatives --set java $JAVA_HOME/bin/java
                    sudo update-alternatives --set javac $JAVA_HOME/bin/javac
                    
                    # Verificações
                    echo "Java home: $JAVA_HOME"
                    echo "Java version:"
                    $JAVA_HOME/bin/java -version
                    echo "Which java:"
                    which java
                    echo "Maven version:"
                    ./mvnw --version
                    
                    # Executar build
                    ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker-compose down || true
                    docker-compose up -d
                '''
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        failure {
            echo 'Pipeline falhou! Verifique os logs para detalhes.'
        }
    }
}