pipeline {
    agent {
        docker {
            image 'eclipse-temurin:21-jdk'
            args '-v $HOME/.m2:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

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
            script {
                // Força o uso do Java 21
                def javaHome = '/usr/lib/jvm/java-21-openjdk-amd64'
                
                sh """
                    # Configuração definitiva do ambiente
                    unset JAVA_HOME
                    unset PATH
                    export JAVA_HOME=${javaHome}
                    export PATH="${javaHome}/bin:/usr/local/bin:/usr/bin:/bin"
                    
                    # Verificações
                    echo "Java home: \$JAVA_HOME"
                    echo "Java version:"
                    ${javaHome}/bin/java -version
                    echo "Maven version:"
                    ./mvnw --version
                    
                    # Build
                    ./mvnw clean package -DskipTests
                """
            }
        }
    }
            stage('Debug Environment') {
            steps {
                sh '''
                    echo "=== Verificando Ambiente ==="
                    echo "Java alternatives:"
                    ls -l /etc/alternatives/java
                    echo "Java links:"
                    ls -l /usr/bin/java
                    echo "Java installations:"
                    ls -l /usr/lib/jvm/
                    echo "Current Java:"
                    which java
                    readlink -f $(which java)
                    java -version
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