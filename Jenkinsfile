pipeline {
  agent none
  stages {
    stage('checkout scm') {
      steps {
        git(url: 'https://github.com/Goodket1/Declarative_Pipeline.git', poll: true)
      }
    }
  }
}