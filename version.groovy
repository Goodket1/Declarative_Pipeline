def URL = 'https://artifactory.legsup.co.uk/artifactory/version/com/efsavage/web_app/maven-metadata.xml'
HttpURLConnection connection = (HttpURLConnection) baseUrl.openConnection();
connection.addRequestProperty("Accept", "application/json")
def basicAuth = "admin:password".getBytes().encodeBase64().toString()
connection.addRequestProperty("Authorization", "Basic ${basicAuth}")

def metadata = new XmlSlurper().parse(URL)
def list = []
metadata.versioning.versions.version.each{
  list.add(it)
}
return list.reverse(true)
