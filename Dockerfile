# ============================================
# Afetto — Dockerfile
# FIAP 2026 — DevOps Tools & Cloud Computing — Sprint 3
# ============================================

FROM gradle:8.14-jdk21 AS build

LABEL maintainer="Afetto Team"
LABEL version="1.0"
LABEL description="API REST Afetto — Spring Boot + MySQL"

WORKDIR /app

COPY . .

RUN gradle clean build -x test

FROM eclipse-temurin:21-jre-jammy

LABEL application="afetto-api"

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

RUN useradd -ms /bin/bash afettouser

USER afettouser

ENTRYPOINT ["java", "-jar", "app.jar"]