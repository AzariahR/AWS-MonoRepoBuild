FROM amazoncorretto:11.0.6
EXPOSE 8081
VOLUME /tmp
RUN ls -la /
COPY ./build/libs/AwsBatchJob-1.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
