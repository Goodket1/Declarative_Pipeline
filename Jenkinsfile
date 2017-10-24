pipeline{
 agent {
    node {
        label 'CI_env'
        customWorkspace '/var/jenkins'
    }
 }
   tools {
     maven 'm3'
   }
   environment {
      def server = Artifactory.newServer url: 'http://10.5.0.12:8081/artifactory/', username: 'admin', password: 'password'
      buildInfo = Artifactory.newBuildInfo()
      def workspace = pwd()
      def tomcat_path = '/opt/tomcat/webapps/ROOT/'

      // Servers
      slave = '10.5.0.14'
      QA_server = '10.5.0.15'
      artifactory_server = 'http://10.5.0.12:8081/artifactory/'
   }
 stages{
    stage('checkout SCM'){
        steps {
            checkout scm
        }
    }
    stage ("initialize") {
      steps {
        sh '''
            echo "PATH = ${PATH}"
            echo "M2_HOME = ${M2_HOME}"
            '''
        }
    }

    stage('SonarQube analysis') {
       when { not { branch 'master'}}
       steps {
         sh "echo OK"
       }
    }
    stage (' Build '){
      steps{
       dir( 'app' ) {
           script {
                sh 'mvn clean verify'
                def server = Artifactory.newServer url: 'http://10.5.0.12:8081/artifactory/', username: 'admin', password: 'password'
                def rtMaven = Artifactory.newMavenBuild()
                rtMaven.resolver server: server, releaseRepo: 'web_app', snapshotRepo: 'web_app'
                rtMaven.deployer server: server, releaseRepo: 'web_app', snapshotRepo: 'web_app'
                rtMaven.tool = 'm3'
                def buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean install'
                buildInfo.env.capture = true
                server.publishBuildInfo buildInfo
                def pom = readMavenPom file: 'pom.xml'
                id = pom.artifactId
                version = pom.version
           }
       }
    }
   }
 }
}
