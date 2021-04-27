FROM openjdk:8-jdk-alpine
ENV JAVA_OPTS=""
VOLUME /tmp
ADD target/currency.jar app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
