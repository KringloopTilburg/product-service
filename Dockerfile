FROM openjdk:21-slim
EXPOSE 8082
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/api-gateway.jar

CMD ["java", "-jar", "api-gateway.jar"]