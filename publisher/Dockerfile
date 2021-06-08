FROM adoptopenjdk:11-jre-hotspot
RUN mkdir /app
COPY /build/libs/publisher-0.0.1-SNAPSHOT.jar /app
CMD ["java", "-jar", "/app/publisher-0.0.1-SNAPSHOT.jar"]