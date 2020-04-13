FROM amazoncorretto:11.0.6
EXPOSE 8085
VOLUME /tmp
COPY ./build/libs/BatchDataProvider-1.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
