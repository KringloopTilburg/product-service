FROM openjdk:21-slim
EXPOSE 8082
WORKDIR /app

ARG JAR_FILE
COPY ${JAR_FILE} /app/productservice.jar
CMD ["java", "-jar", "productservice.jar"]