pipeline {
    agent any

    triggers {
        pollSCM('')
      }

    stages {
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
                archiveArtifacts artifacts: '**/target/*.jar'
            }
        }
    }

}