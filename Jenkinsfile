pipeline {
    agent any

    tools{
        maven "M3"
    }
    stages {
        stage('Build') {
            steps {
            sh "mvn clean compile"
            }
        }
    }
    stages {
        stage('Test') {
            steps {
            sh "mvn test"
            }
        }
    }
    stages {
        stage('Deploy') {
            steps {
            sh "mvn clean heroku:deploy"
            }
        }
    }
}
