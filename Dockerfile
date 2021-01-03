FROM openjdk:8-jdk-alpine
EXPOSE 8087
COPY build/libs/product-aggregator-0.0.1.jar app1.jar
ENTRYPOINT ["java","-jar","/app1.jar"]