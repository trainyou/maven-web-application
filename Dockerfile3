FROM maven as bld
WORKDIR /maven
COPY . .
RUN mvn clean package

FROM heartocean/working:tomcat
COPY --from=bld /maven/target/maven-web-application.war /app/tomcat/webapps/maven-web-application.war
CMD ["/app/tomcat/bin/catalina.sh", "run"]
