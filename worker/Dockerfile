FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle

RUN ./gradlew :manager:dependencies --no-daemon

COPY worker worker
COPY generated-model generated-model

RUN chmod +x gradlew

RUN ./gradlew :worker:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/worker/build/libs/worker-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]