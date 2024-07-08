FROM eclipse-temurin:17.0.11_9-jdk-ubi9-minimal

WORKDIR /demo

COPY target/demo-0.0.1-SNAPSHOT.jar /demo/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]