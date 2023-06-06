FROM gradle:7.6.1-jdk17-alpine AS BUILD
LABEL authors="Dylan Lannuzel"

COPY ./ ./

RUN gradle build

FROM openjdk:17-alpine

CMD ["pwd"]

COPY --from=BUILD ./build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

ENTRYPOINT ["top", "-b"]