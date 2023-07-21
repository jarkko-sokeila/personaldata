FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY build/libs/*.jar personaldata.jar

# expose port
EXPOSE 8080

ENTRYPOINT ["java","-jar","/personaldata.jar"]