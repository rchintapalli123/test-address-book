FROM amazoncorretto:17.0.7-alpine
WORKDIR /app
EXPOSE 8080
COPY build/libs/*.jar  /app/addressbook.jar
ENTRYPOINT [ "java", "-jar", "/app/addressbook.jar" ]