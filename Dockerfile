FROM tomcat:8.0
USER root
ARG PACKAGE_VERSION
RUN echo "${PACKAGE_VERSION}" >> /usr/local/tomcat/webapps/version.txt
COPY project.war /tmp/project.war
RUN rm -r /usr/local/tomcat/webapps/ROOT/* && unzip /tmp/project.war -d /usr/local/tomcat/webapps/ROOT/

ENTRYPOINT ["catalina.sh", "run"]