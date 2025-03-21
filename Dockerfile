
# 第一阶段:编译Maven项目
FROM maven:3.9.9-amazoncorretto-21-alpine AS build
WORKDIR /app

ARG PROJECT_NAME

COPY . .

RUN mvn clean package -DskipTests=true


FROM openjdk:21-jdk-slim
ARG PROJECT_NAME
ARG VERSION

ENV AGENT_FILE_PATH="/app/opentelemetry-javaagent.jar"

ENV APP_VERSION=${VERSION}
ENV APP_NAME=${PROJECT_NAME}
ENV ENVIRONMENT="pat"
ENV ENDPOINT="http://tracing-analysis-dc-hk.aliyuncs.com/adapt_1g4jj9bdnus@7d441d3f6749462_1g4jj9bdnus@53df7ad2afe8301"
ENV OPTS="-javaagent:${AGENT_FILE_PATH} \
-Dotel.resource.attributes=service.name=${APP_NAME},service.version=${APP_VERSION},deployment.environment=${ENVIRONMENT} \
-Dotel.exporter.otlp.protocol=http/protobuf \
-Dotel.exporter.otlp.traces.endpoint=${ENDPOINT}/api/otlp/traces \
-Dotel.exporter.otlp.metrics.endpoint=${ENDPOINT}/api/otlp/metrics \
-Dotel.logs.exporter=none "

ENV APP_FILE=${PROJECT_NAME}-${VERSION}.jar
WORKDIR /app
COPY --from=build /app/${PROJECT_NAME}/target/${PROJECT_NAME}-${VERSION}.jar /app/
COPY ./opentelemetry-javaagent.jar /app/

ENTRYPOINT ["sh","-c", "exec java ${OPTS} -jar ${APP_FILE}"]


