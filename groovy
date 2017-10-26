def metadata = new XmlSlurper().parse("http://10.5.0.12:8081/artifactory/snapshots/com/efsavage/web_app/maven-metadata.xml")
def list = []
metadata.versioning.versions.version.each{
  list.add(it)
}
return list.reverse(true)
