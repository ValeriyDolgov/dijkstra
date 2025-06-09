FROM openjdk:17-jdk-slim

EXPOSE 8080
VOLUME /tmp

ADD target/dijkstra-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'

RUN unlink /etc/localtime && ln -s /usr/share/zoneinfo/Asia/Almaty /etc/localtime

COPY src/main/resources src/main/resources

ENTRYPOINT ["java", "-Xmx4g", "-Duser.timezone=Asia/Qyzylorda", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]