FROM openjdk:8-jdk-alpine

COPY . /app
WORKDIR /app
VOLUME /app/output

COPY Crontab /etc/crontabs/root

RUN mkdir -p ~/.gradle && echo "org.gradle.daemon=false" >> ~/.gradle/gradle.properties

RUN ./gradlew build

CMD crond -f -d 8