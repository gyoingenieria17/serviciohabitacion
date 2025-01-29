FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/serviciohabitacion-0.0.1.jar
COPY ${JAR_FILE} app_serviciohabitacion.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","/app_serviciohabitacion.jar"]