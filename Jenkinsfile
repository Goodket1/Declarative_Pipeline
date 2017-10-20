
pipeline {
    agent any
    stages {
        stage('Checkout scm') {
            steps {
                checkout scm
            }
        }
        stage('build') {
            steps {
                git branch: 'master', credentialsId: 'e.joe-gitlab', url: 'http://70.121.224.108/gitlab/cicd/spring-petclinic.git'
                sh 'mvn clean package'
            }
        }
        stage('verify') {
            steps {
                sh 'ls -alF target'
            }
        }        
        stage('docker') {
                withDockerRegistry([credentialsId: 'redii-e.joe', url: 'https://sds.redii.net']) {
                    def app = docker.build("sds.redii.net/e-joe/spring-pet-clinic-demo:v1",'.')
                    app.push()
                }
        }
    }
}
