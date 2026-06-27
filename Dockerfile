# syntax=docker/dockerfile:1
FROM --platform=linux/amd64 eclipse-temurin:25-jdk-alpine AS build

RUN apk add --no-cache curl unzip

ARG GRADLE_VERSION=9.6.0
RUN curl -fsSL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o /tmp/gradle.zip \
    && unzip -q /tmp/gradle.zip -d /opt \
    && rm /tmp/gradle.zip
ENV PATH="/opt/gradle-${GRADLE_VERSION}/bin:${PATH}"

WORKDIR /workspace

COPY settings.gradle.kts build.gradle.kts ./

RUN gradle dependencies --no-daemon

COPY src src
RUN gradle bootJar --no-daemon

FROM --platform=linux/amd64 eclipse-temurin:25-jre-alpine AS runtime

RUN addgroup -S app && adduser -S app -G app
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar
# create logs directory as root, set ownership for the non-root `app` user
RUN mkdir -p /app/logs \
    && chown -R app:app /app/logs
VOLUME ["/app/logs"]

USER app
EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
