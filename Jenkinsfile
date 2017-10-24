pipeline{
 agent any
 tools {
   maven 'm3'
    }
    environment {
      def server = Artifactory.newServer url: 'http://10.5.0.12:8081/artifactory/', username: 'admin', password: 'password'
      buildInfo = Artifactory.newBuildInfo()
      def workspace = pwd()
      def tomcat_path = '/opt/tomcat/webapps/ROOT/'

      // Repository on Artifactory
      masterrepo ='version'
      stagerepo ='rc'
      devrepo =  'hotfix'

      // Servers
      dev_server = '10.5.0.14'
      stage_server = '10.5.0.15'
      prod_server = '10.5.0.16'
      http_dev_server = "http://10.5.0.14:8080/"
      http_stage_server = 'http://10.5.0.15:8080/'
      http_prod_server = 'http://10.5.0.16:8080'
      artifactory_server = 'http://10.5.0.12:8081/artifactory/'
    }
 stages{
    stage('checkout SCM'){
        steps {
            checkout scm
            script {
              if (env.BRANCH_NAME == 'master') {
                  env.http_server = "${http_prod_server}"
                  env.sshserver = "${prod_server}"
                  env.reponame = "${masterrepo}"
              } else if ( env.BRANCH_NAME == 'stage'){
                  env.http_server = "${http_stage_server}${BRANCH_NAME}${env.BUILD_NUMBER}"
                  env.sshserver = "${stage_server}"
                  env.reponame = "${stagerepo}"
                  tomcat_path = "${tomcat_path}/${BRANCH_NAME}${env.BUILD_NUMBER}"
              }else {
                  env.http_server = "${http_dev_server}${BRANCH_NAME}${env.BUILD_NUMBER}"
                  env.sshserver = "${dev_server}"
                  env.reponame = "${devrepo}"
                  tomcat_path = "${tomcat_path}${BRANCH_NAME}${env.BUILD_NUMBER}"
                  }
            }
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
           if (env.BRANCH_NAME == 'master' ){
                input 'Do you approve  publish to PROD?'
           }
           else if (env.BRANCH_NAME == 'stage' ){
                def server = Artifactory.newServer url: 'http://10.5.0.12:8081/artifactory/', username: 'admin', password: 'password'
                def rtMaven = Artifactory.newMavenBuild()
                rtMaven.resolver server: server, releaseRepo: reponame, snapshotRepo: reponame
                rtMaven.deployer server: server, releaseRepo: reponame, snapshotRepo: reponame
                rtMaven.tool = 'm3'
                def buildInfo = rtMaven.run pom: 'pom.xml', goals: 'install'
                buildInfo.env.capture = true
                server.publishBuildInfo buildInfo
                def pom = readMavenPom file: 'pom.xml'
                id = pom.artifactId
                version = pom.version
           }
           else {
                sh 'mvn clean verify'
                def pom = readMavenPom file: 'pom.xml'
                id = pom.artifactId
                version = pom.version
          }
          }
        }
      }
    }
    stage ('Deploy to Server'){
      steps{
         script {
              if (env.BRANCH_NAME == 'master' ){
                ansiblePlaybook(
                playbook: 'jenkins/deploy.yml',
                inventory: 'jenkins/inventory',
                extras: "-e version=${env.latest} -e id=${env.artifactId} -e artifactory=${artifactory_server} -e host=${sshserver} -e reponame=${reponame}  -e build=${env.latest} -e path=${tomcat_path}"
                )
              }
              else if (env.BRANCH_NAME == 'stage' ){
                ansiblePlaybook(
                playbook: 'jenkins/deploy.yml',
                inventory: 'jenkins/inventory',
                extras: "-e version=${version} -e id=${id} -e artifactory=${artifactory_server} -e host=${sshserver} -e reponame=${reponame} -e build=${env.BUILD_NUMBER} -e path=${tomcat_path}")
              }
              else {
                sh "mkdir /tmp/${BRANCH_NAME}${env.BUILD_NUMBER} && \
                  unzip ${env.workspace}/app/target/${id}-${version}.war -d /tmp/${BRANCH_NAME}${env.BUILD_NUMBER}/"
                 sh "scp -r -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null  /tmp/${BRANCH_NAME}${env.BUILD_NUMBER} root@${dev_server}:${tomcat_path}"
              }

         }
      }
    }
    stage ('Test URL '){
      steps{
         script {
              if (env.BRANCH_NAME == 'master' ){
                 def response = httpRequest env.http_server
                 println("Status: "+response.status)
              }
              else if (env.BRANCH_NAME == 'stage' ){
                 def response = httpRequest env.http_server
                 println("Status: "+response.status)
                 input 'Is build succeed?'
                 def server = Artifactory.newServer url: 'http://10.5.0.12:8081/artifactory/', username: 'admin', password: 'password'
                 def rtMaven = Artifactory.newMavenBuild()
                 rtMaven.resolver server: server, releaseRepo: reponame, snapshotRepo: reponame
                 rtMaven.deployer server: server, releaseRepo: reponame, snapshotRepo: reponame
                 rtMaven.tool = 'm3'
                 def buildInfo = rtMaven.run pom: 'app/pom.xml', goals: 'clean'
                 def promotionConfig = [
                    'buildName'          : buildInfo.name,
                    'buildNumber'        : buildInfo.number,
                    'targetRepo'         : 'version',
                    'comment'            : 'this is the promotion comment',
                    'sourceRepo'         : 'rc',
                    'status'             : 'Released',
                    'includeDependencies': true,
                    'copy'               : true
                 ]
                 server.promote promotionConfig
              }
              else {
                 def response = httpRequest env.http_server
                 println("Status: "+response.status)
                 sh "rm -r  /tmp/${BRANCH_NAME}${env.BUILD_NUMBER}"
              }
         }
      }
    }
 }
}

