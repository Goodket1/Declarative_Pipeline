def URL = 'http://10.17.11.223:8091/artifactory/snapshots/com/efsavage/web_app/maven-metadata.xml'
def metadata = new XmlSlurper().parse(URL)
def list = []
metadata.versioning.versions.version.each{
  list.add(it)
}
return list.reverse(true)
println list[0]


pipeline{
  agent  {
    node {
     label 'CI_env'
        customWorkspace '/var/jenkins'
    }
 }
stages ('build') {
  stage ('push') {
    steps {
      script{

def server = Artifactory.newServer url: 'https://artifactory.legsup.co.uk/artifactory/', username: 'admin', password: 'Tf1cda11'
def artDocker= Artifactory.docker username: 'admin', password: 'Tf1cda11'
def dockerInfo = artDocker.push 'artifactory.legsup.co.uk:8082/hello-world:latest', 'docker'
    server.publishBuildInfo(dockerInfo)
}
}
}
}
}
