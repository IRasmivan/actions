FROM openjdk:8-jdk-alpine
MAINTAINER Rasmivan Ilangovan
COPY target/peers-auction-task-0.0.1-SNAPSHOT.jar /app.jar
CMD ["java","-cp","/app.jar","com.peers.task.auction.Application"]