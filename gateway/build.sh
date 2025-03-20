#!/bin/bash
docker build --build-arg PROJECT_NAME=gateway --build-arg VERSION=0.0.1-SNAPSHOT  -t gateway:0.0.1-SNAPSHOT .

docker run -d --name gateway -e SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=127.0.0.1:8848 gateway:0.0.1-SNAPSHOT