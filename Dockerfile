FROM openjdk:11.0.11-jdk
ARG JAR_FILE=build/libs/*.jar
ENV TZ=Asia/Seoul
COPY ${JAR_FILE} /app/G-Makers.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev", "-Dfile.encoding=UTF-8", "-jar", "/app/G-Makers.jar"]