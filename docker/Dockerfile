FROM amazoncorretto:25-alpine-full
LABEL maintainer="Nisarg Panchal"
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080