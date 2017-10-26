def URL = 'http://10.17.11.223:8091/artifactory/snapshots/com/efsavage/web_app/maven-metadata.xml'
def metadata = new XmlSlurper().parse(URL)
def list = []
metadata.versioning.versions.version.each{
  list.add(it)
}
return list.reverse(true)
println list