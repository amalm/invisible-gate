FROM openjdk:8-jdk-alpine
MAINTAINER Anders Malmborg "maas@skidata.com"
ENV APPROOT="/app" \
    APP="facerec-server.jar" \
    JAVA_OPTS=""
VOLUME ["/tmp", "$APPROOT", "$APPROOT/config"] 
ADD ["maven/", "$APPROOT"]
RUN ["addgroup", "facerec"]
RUN ["adduser", "-H", "-D", "-G", "facerec", "facerec"]
USER facerec
WORKDIR "$APPROOT"
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "cd /app && java $JAVA_OPTS \
   -Djava.security.egd=file:/dev/./urandom -jar /app/facerec-server.jar" ]