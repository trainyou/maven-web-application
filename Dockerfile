FROM heartocean/working:tomcat
COPY /target/maven-web-application.war /app/tomcat/webapps/maven-web-application.war
