FROM docker.conygre.com:5000/java

WORKDIR /app

COPY . .

RUN ./mvnw package

FROM openjdk:8-alpine

WORKDIR /app

COPY --from=build /app/target/TodoDemo-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "/app/TodoDemo-0.0.1-SNAPSHOT.jar"]
