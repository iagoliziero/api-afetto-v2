# ============================================
# Afetto — Dockerfile
# FIAP 2026 — DevOps Tools & Cloud Computing
# Também é o que o Render usa (serviço Web com runtime Docker).
# ============================================

# --- Stage 1: Build ---
    FROM gradle:8.14-jdk21 AS build

    LABEL maintainer="Afetto Team"
    LABEL version="1.0"
    LABEL description="API REST Afetto — Spring Boot + H2"
    
    WORKDIR /app
    
    COPY . .
    
    # Só o jar executável (bootJar). O `build` também gera o "-plain.jar", e
    # com dois arquivos o COPY *.jar do estágio 2 falha.
    RUN gradle clean bootJar --no-daemon
    
    # --- Stage 2: Runtime ---
    FROM eclipse-temurin:21-jre-jammy
    
    LABEL application="afetto-api"
    
    WORKDIR /app
    
    COPY --from=build /app/build/libs/*.jar app.jar
    
    # Local: 8080. No Render, a API escuta na PORT que ele define
    # (server.port=${PORT:8080} no application.properties).
    EXPOSE 8080
    
    # Criar usuário sem privilégios administrativos
    RUN useradd -ms /bin/bash afettouser
    
    # Rodar como usuário sem root
    USER afettouser
    
    # Instância pequena (Render gratuito: 512 MB): o Java usa até 75% da memória
    # do contêiner (o padrão é 25%), com o coletor mais leve, e sobe mais rápido
    # (TieredStopAtLevel=1) — o que encurta a espera quando o Render "acorda" a API.
    ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-XX:+UseSerialGC", "-XX:TieredStopAtLevel=1", "-jar", "app.jar"]
