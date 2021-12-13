FROM openjdk:11
EXPOSE 8090
ADD target/springboot-mongo-docker.jar springboot-mongo-docker.jar
ENTRYPOINT ["java","-jar","/springboot-mongo-docker.jar"]