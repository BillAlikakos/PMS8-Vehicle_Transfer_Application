FROM eclipse-temurin:17.0.11_9-jdk-ubi9-minimal

WORKDIR /application

COPY target/userAuth-0.0.1-SNAPSHOT.jar /application/app.jar

EXPOSE 8082

CMD ["java", "-jar", "app.jar"]