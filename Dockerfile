FROM openjdk:11.0.11-jdk
ARG JAR_FILE=build/libs/*.jar
ENV TZ=Asia/Seoul
COPY ${JAR_FILE} /app/G-Makers.jar
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8","-Djasypt.encryptor.password=opgg", "-jar", "/app/G-Makers.jar"]