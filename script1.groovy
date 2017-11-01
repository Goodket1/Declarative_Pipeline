def URL = 'https://artifactory.legsup.co.uk/artifactory/version/com/efsavage/web_app/maven-metadata.xml'
def metadata = new XmlSlurper().parse(URL)
def list = []
metadata.versioning.versions.version.each{
  list.add(it)
}
return list.reverse(true)
