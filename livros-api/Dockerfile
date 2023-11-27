FROM maven:3.9.5-eclipse-temurin-17-alpine AS build-livros
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN mkdir imagens
COPY --from=build-livros ./app/target/*.jar ./app.jar
EXPOSE 8080

ENTRYPOINT java -jar app.jar
#ENTRYPOINT java -jar -Dspring.profiles.active=development app.jar