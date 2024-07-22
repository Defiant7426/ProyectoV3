# Etapa de construcción
FROM gradle:7.3.3-jdk17 AS builder
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle build.gradle /home/gradle/src/

RUN gradle build --no-daemon --refresh-dependencies

COPY --chown=gradle:gradle . /home/gradle/src

RUN gradle build --no-daemon

# Etapa de ejecución
FROM openjdk:17
EXPOSE 8080
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/wave-service-1.0-SNAPSHOT.jar /app/wave-service.jar
CMD ["java", "-jar", "wave-service.jar"]
