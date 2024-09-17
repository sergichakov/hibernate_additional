FROM tomcat:10-jdk17-temurin
LABEL maintainer="sergejsmelchakov@gmail.com"
ARG JAR_FILE=target/*.war
ADD ${JAR_FILE} /usr/local/tomcat/webapps/
#EXPOSE 5432
EXPOSE 8080
CMD ["catalina.sh", "run"]




#   eclipse-temurin:17-jdk-noble
#FROM eclipse-temurin:17-jre-alpine
#ARG JAR_FILE=target/*.war
#RUN echo "hello ${JAR_FILE}"
#RUN echo $(ls  src)
#COPY ${JAR_FILE} app.war
#EXPOSE 8080
#EXPOSE 5432
#ENTRYPOINT ["java", "-jar", "/app.war"]