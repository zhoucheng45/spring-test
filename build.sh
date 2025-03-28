#!/bin/bash
docker build --build-arg PROJECT_NAME=gateway --build-arg VERSION=0.0.1-SNAPSHOT  -t gateway:0.0.1-SNAPSHOT .


docker buildx build --build-arg PROJECT_NAME=gateway --build-arg VERSION=0.0.1-SNAPSHOT -t registry-intl.cn-hongkong.aliyuncs.com/my-link/test:gateway-0.0.1-SNAPSHOT --platform linux/amd64,linux/arm64 . --push
docker buildx build --build-arg PROJECT_NAME=front --build-arg VERSION=0.0.1-SNAPSHOT -t registry-intl.cn-hongkong.aliyuncs.com/my-link/test:front-0.0.1-SNAPSHOT --platform linux/amd64,linux/arm64 . --push
docker buildx build --build-arg PROJECT_NAME=backend --build-arg VERSION=0.0.1-SNAPSHOT -t registry-intl.cn-hongkong.aliyuncs.com/my-link/test:backend-0.0.1-SNAPSHOT --platform linux/amd64,linux/arm64 . --push


docker run -d --name gateway \
 -e APP_NAME=test-gateway \
 -e SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=172.23.0.2:8848 \
 -e ENDPOINT=http://tracing-analysis-dc-hk.aliyuncs.com/adapt_1g4jj9bdnus@7d441d3f6749462_1g4jj9bdnus@53df7ad2afe8301 \
  registry-intl.cn-hongkong.aliyuncs.com/my-link/test:gateway-0.0.1-SNAPSHOT


# 切换回默认驱动
docker buildx use default

# 构建并加载镜像
docker buildx build --build-arg PROJECT_NAME=gateway --build-arg VERSION=0.0.1-SNAPSHOT -t registry-intl.cn-hongkong.aliyuncs.com/my-link/test:gateway-0.0.1-SNAPSHOT . --load

docker run -v ./:/workspace \
  -v ./config.json:/kaniko/.docker/config.json \
  gcr.io/kaniko-project/executor:latest \
  --dockerfile=/workspace/Dockerfile \
  --context=dir:///workspace \
  --destination=registry-intl.cn-hongkong.aliyuncs.com/my-link/test:gateway-0.0.1-SNAPSHOT \
  --build-arg PROJECT_NAME=gateway \
  --build-arg VERSION=0.0.1-SNAPSHOT