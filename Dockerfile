FROM tomcat:8.0
USER root
# Place the code version inside the webapps directory
ARG PACKAGE_VERSION
RUN echo "${PACKAGE_VERSION}" >> /usr/local/tomcat/webapps/version.txt
#COPY project.war /tmp/project.war
RUN rm -r /usr/local/tomcat/webapps/ROOT/*
#&& cp  /tmp/project.war /usr/local/tomcat/webapps/ROOT/
COPY project.war /usr/local/tomcat/webapps/ROOT/

CMD ["catalina.sh", "run"]