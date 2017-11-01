@Grab(group='org.codehaus.groovy.modules.http-builder',
      module='http-builder', version='0.5.0-RC2' )
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.HTTPBuilder;
import groovy.json.JsonSlurper

def baseUrl = new URL('https://artifactory.legsup.co.uk/artifactory/version/com/efsavage/web_app/maven-metadata.xml'')
HttpURLConnection connection = (HttpURLConnection) baseUrl.openConnection();
connection.addRequestProperty("Accept", "application/json")
def basicAuth = "admin:Tf1cda11".getBytes().encodeBase64().toString()
connection.addRequestProperty("Authorization", "Basic ${basicAuth}")
connection.with {
    doOutput = true
           requestMethod = 'GET'
           def meta = content.text
           def restResponse = meta
           def list1 = new JsonSlurper().parseText( restResponse )
           def names = list1.tags
           def list = []

           names.each {
             list.add(it)
              }
           print list.reverse(true)
         }
