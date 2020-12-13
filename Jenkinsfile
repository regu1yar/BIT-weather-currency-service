pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }

    stages {
        stage('Package') {
            steps {
                sh 'mvn package'
                archiveArtifacts artifacts: '**/target/*.jar'
            }
        }
    }

}