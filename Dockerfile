FROM docker.io/library/openjdk:21-jdk-slim
ARG PROJECT_NAME
ARG VERSION
ENV APP_VERSION=${VERSION}
ENV APP_NAME=${PROJECT_NAME}
ENV ENVIRONMENT="pat"
ENV ENDPOINT=""
ENV APP_FILE=${PROJECT_NAME}-${VERSION}.jar
WORKDIR /app
COPY ./opentelemetry-javaagent.jar /app/
COPY ./${PROJECT_NAME}/target/${PROJECT_NAME}-${VERSION}.jar /app/

ENTRYPOINT ["sh","-c", "exec java -javaagent:/app/opentelemetry-javaagent.jar \
                                  -Dotel.resource.attributes=service.name=${APP_NAME},service.version=${APP_VERSION},deployment.environment=${ENVIRONMENT} \
                                  -Dotel.exporter.otlp.protocol=http/protobuf \
                                  -Dotel.exporter.otlp.traces.endpoint=${ENDPOINT}/api/otlp/traces \
                                  -Dotel.exporter.otlp.metrics.endpoint=${ENDPOINT}/api/otlp/metrics \
                                  -Dotel.instrumentation.logback-appender.enabled=true \
                                  -Dotel.logs.exporter=none  -jar ${APP_FILE}"]


