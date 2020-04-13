FROM amazoncorretto:11.0.6
#MAINTAINER its.azariah@gmail.com
EXPOSE 3333
RUN ls -la /
COPY apps/MonoFirstApp/build/libs/MonoFirstApp-1.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
