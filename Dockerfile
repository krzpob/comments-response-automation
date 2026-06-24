# syntax=docker/dockerfile:1
FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine AS build

RUN apk add --no-cache curl unzip

ARG GRADLE_VERSION=8.7
RUN curl -fsSL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o /tmp/gradle.zip \
    && unzip -q /tmp/gradle.zip -d /opt \
    && rm /tmp/gradle.zip
ENV PATH="/opt/gradle-${GRADLE_VERSION}/bin:${PATH}"

WORKDIR /workspace

COPY settings.gradle.kts build.gradle.kts ./

RUN gradle dependencies --no-daemon -q

COPY src src
RUN gradle bootJar --no-daemon -q

FROM --platform=linux/amd64 eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S app && adduser -S app -G app
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
