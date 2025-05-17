pipeline {
    agent {
        docker {
            image 'eclipse-temurin:21-jdk'
            args '-v $HOME/.m2:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock'
            reuseNode true
        }
    }
    stages {
        stage('Build') {
            steps {
                sh '''
                    echo "Java path: $(which java)"
                    echo "Java version:"
                    java -version
                    ./mvnw clean package -DskipTests
                '''
            }
        }
    }
}