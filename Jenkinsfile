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
      def workspace = pwd()
      def tomcat_path = '/opt/tomcat/webapps/ROOT/'
      // Servers
      slave = '10.5.0.14'
      QA_server = '10.5.0.15'
      artifactory_server = 'http://10.5.0.12:8081/artifactory/'
      reponame = 'snaphots'
      http_server = "http://${slave}:8080/${env.BUILD_NUMBER}"
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
                sh 'mvn clean install'
           }
       }
    }
   }
   stage ('Test if get 200 Ok'){
    steps{
      dir( 'app' ) {
        script{
           def pom = readMavenPom file: 'pom.xml'
           id = pom.artifactId
           version = pom.version
           sh "unzip ${env.workspace}/app/target/${id}-${version}.war -d  ${env.tomcat_path}${env.BUILD_NUMBER}/"
           def response = httpRequest env.http_server
           println("Status: "+response.status)
           configFileProvider(
             [configFile(fileId: 'bb69bc1a-f3cd-4af9-a106-551e55851850', variable: 'MAVEN_SETTINGS')]) {
                sh "mvn -s $MAVEN_SETTINGS deploy:deploy-file -DartifactId=${id} -Dversion=${version}"
              }
        }
      }
    }
   }
    stage ('Deploy to Server'){
      steps{
         script {
            sh "chmod 400 jenkins/id_rsa"
            ansiblePlaybook(
            playbook: 'jenkins/deploy.yml',
            inventory: 'jenkins/inventory',
            extras: "-e version=${version} -e id=${id} -e artifactory=${artifactory_server} -e host=${QA_server} -e reponame=${reponame} -e build=${env.BUILD_NUMBER} -e path=${tomcat_path}")
         }
      }
    }

 }
}
