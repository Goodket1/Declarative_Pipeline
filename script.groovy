def URL = 'https://artifactory.legsup.co.uk/artifactory/api/docker/docker/v2/web_app/tags/list'

def metadata = new XmlSlurper().parse(URL)
def list = []
metadata.tags.each{
  list.add(it)
}
return list.reverse(true)

