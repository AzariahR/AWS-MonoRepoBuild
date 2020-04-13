FROM amazoncorretto:11.0.6
#MAINTAINER its.azariah@gmail.com
EXPOSE 3033
COPY apps/MonoSecondApp/build/libs/MonoSecondApp-1.0.jar app.jar
#RUN ls -la /
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
