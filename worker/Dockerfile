# ==========================
# 1. Базовый образ для сборки
# ==========================
FROM eclipse-temurin:17-jdk-alpine AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем Gradle Wrapper и настройки (чтобы кешировать зависимости)
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle

# Копируем исходники всех модулей
COPY worker worker
COPY generated-model generated-model

# Даем права на выполнение Gradle Wrapper
RUN chmod +x gradlew

# Собираем приложение (меняем путь, если другой)
RUN ./gradlew :worker:bootJar -x test

# ==========================
# 2. Финальный образ для запуска
# ==========================
FROM eclipse-temurin:17-jre-alpine

# Рабочая директория в контейнере
WORKDIR /app

# Копируем JAR из builder-а
COPY --from=builder /app/worker/build/libs/worker-*.jar app.jar

# Запускаем сервис
ENTRYPOINT ["java", "-jar", "app.jar"]