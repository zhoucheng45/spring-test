FROM openjdk:21-jdk-slim
ARG PROJECT_NAME
ARG VERSION

ENV AGENT_FILE_PATH="/app/opentelemetry-javaagent.jar"

ENV APP_VERSION=${VERSION}
ENV APP_NAME=${PROJECT_NAME}
ENV ENVIRONMENT="pat"
ENV ENDPOINT=""
ENV OPTS="-javaagent:${AGENT_FILE_PATH} \
-Dotel.resource.attributes=service.name=${APP_NAME},service.version=${APP_VERSION},deployment.environment=${ENVIRONMENT} \
-Dotel.exporter.otlp.protocol=http/protobuf \
-Dotel.exporter.otlp.traces.endpoint=${ENDPOINT}/api/otlp/traces \
-Dotel.exporter.otlp.metrics.endpoint=${ENDPOINT}/api/otlp/metrics \
-Dotel.logs.exporter=none "

ENV APP_FILE=${APP_NAME}-${APP_VERSION}.jar
WORKDIR /app
COPY ./target/${APP_FILE} /app/
COPY ./opentelemetry-javaagent.jar /app/

ENTRYPOINT ["sh","-c", "exec java ${OPTS} -jar ${APP_FILE}"]


