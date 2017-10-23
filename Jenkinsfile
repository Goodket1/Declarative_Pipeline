pipeline{
 agent any
    environment {
      def server = Artifactory.newServer url: 'http://10.5.0.12:8081/artifactory/', username: 'admin', password: 'password'
      def workspace = pwd()
      def tomcat_path = '/opt/tomcat/webapps/ROOT/'
      def rtMaven = Artifactory.newMavenBuild()

      // Repository on Artifactory
      masterrepo ='version'
      stagerepo ='rc'
      devrepo =  'hotfix'

      // Servers
      dev_server = '10.5.0.14'
      stage_server = '10.5.0.15'
      prod_server = '10.5.0.16'
      http_dev_server = "http://10.5.0.14:8080/${env.BUILD_NUMBER}"
      http_stage_server = 'http://10.5.0.15:8080/${env.BUILD_NUMBER}'
      http_prod_server = 'http://10.5.0.16:8080'
    }
 stages{
    stage('checkout SCM'){
        steps {
            checkout scm
        }
    }
    stage('SonarQube analysis') {
       when {
         not { branch 'master'}
       }
       steps {
         sh "echo OK"
       }

    }
    stage ('Build'){
       steps{
          dir("app"){
            script {
                def server = Artifactory.newServer url: 'http://10.5.0.12:8081/artifactory/', username: 'admin', password: 'password'
                def rtMaven = Artifactory.newMavenBuild()
                rtMaven.tool = 'm3'
                def buildInfo = rtMaven.run pom: 'pom.xml', goals: 'install'
                def pom = readMavenPom file: 'pom.xml'
                id = pom.artifactId
                version = pom.version
            }
          }
       }
    }
    stage ('Unpack Build'){
       when {
         not { branch 'master'}
         not { branch 'stage' }
       }
         steps{
             script {
                sh "mkdir /tmp/${env.BUILD_NUMBER} && \
                  unzip ${env.workspace}/app/target/${id}-${version}.war -d /tmp/${env.BUILD_NUMBER}/"
             }
         }
    }
    stage ('Copy to Dev Server'){
       when {
         not { branch 'master'}
         not { branch 'stage' }
       }
         steps{
                sh "scp -r -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null  /tmp/${env.BUILD_NUMBER} root@${dev_server}:${tomcat_path}"
         }
    }
    stage ('Smoke Test on Dev server' ) {
       when {
         not { branch 'master'}
         not { branch 'stage' }
       }
        steps{
             script {
               def response = httpRequest http_dev_server
               println("Status: "+response.status)
             }
        }
    }
    stage ('Clean on Dev server'){
       when {
         not { branch 'master'}
         not { branch 'stage' }
       }
       steps{
            script {
              sh "rm -r  /tmp/${env.BUILD_NUMBER}"
            }

       }
    }
  }
}
