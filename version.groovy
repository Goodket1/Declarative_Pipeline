@Grab(group='org.codehaus.groovy.modules.http-builder',
      module='http-builder', version='0.5.0-RC2' )
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.HTTPBuilder;
import groovy.json.JsonSlurper

def basicAuth = "admin:Tf1cda11".getBytes().encodeBase64().toString()
def baseUrl = new URL('https://artifactory.legsup.co.uk/artifactory/version/com/efsavage/web_app/maven-metadata.xml')
HttpURLConnection connection = (HttpURLConnection) baseUrl.openConnection();
connection.setRequestMethod("GET");
connection.addRequestProperty("Authorization", "Basic ${basicAuth}")
connection.connect();
InputStream is = connection.getInputStream();
BufferedReader bufferedReader =
     new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
        def metadata = new XmlSlurper().parseText(System.out)

        }
