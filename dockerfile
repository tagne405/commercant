FROM openjdk:19.0.1
VOLUME /tmp
ADD target/ecommerce2-0.0.1-SNAPSHOT.jar /app2.jar
CMD ["java", "-jar","/app2.jar","--spring.profiles.active-prod"]
EXPOSE 8085