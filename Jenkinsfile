pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                mvn test
            }
        }
    }

    stages {
        stage('Package') {
            steps {
                mvn package
                archiveArtifacts artifacts: '**/target/*.jar'
            }
        }
    }

}