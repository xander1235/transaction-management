FROM openjdk:slim-buster

ENV JAR_FILE transactions-management-*SNAPSHOT.jar
ENV RESOURCES src/main/resources

RUN mkdir /usr/share/loco

ENV PROJECT_HOME /usr/share/loco

EXPOSE 8080

COPY target/$JAR_FILE $PROJECT_HOME/

COPY $RESOURCES/application.properties $PROJECT_HOME/

WORKDIR $PROJECT_HOME

CMD [ "sh", "-c", "java ${JVM} -jar $PROJECT_HOME/$JAR_FILE --spring.config.location=$PROJECT_HOME/application.properties"]
