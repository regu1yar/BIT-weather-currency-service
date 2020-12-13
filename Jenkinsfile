pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                mvn test
            }
        }

        failure {
            mail to: roma_klimovitskiy@mail.ru, subject: 'Your Pipeline failed'
        }

        changed {
            mail to: roma_klimovitskiy@mail.ru, subject: 'The Pipeline status changed'
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