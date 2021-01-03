FROM adoptopenjdk/openjdk14
EXPOSE 8087
COPY build/libs/product-aggregator-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]