FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /workspace
COPY . .

RUN mvn -B -f pom.xml package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && useradd --system --uid 10001 --create-home app

ARG SERVICE
COPY --from=build /workspace/${SERVICE}/target/*.jar /app/app.jar

USER app
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=60.0", "-jar", "/app/app.jar"]
