
package com.example.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
@Component
public class RequestLogFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger("ACCESS_LOG");
    private static final String REQUEST_ID = "requestId";
    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 缓存请求 Body（关键步骤）
        ServerHttpRequest cachedRequest = cacheRequestBody(exchange);

        // 记录请求日志（包含 Body）
        return logRequestWithBody(cachedRequest)
                .then(chain.filter(exchange.mutate().request(cachedRequest).build()));
    }

    // 缓存请求 Body 的方法
    private ServerHttpRequest cacheRequestBody(ServerWebExchange exchange) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody()
                        .reduce(DataBuffer::write)
                        .flatMapMany(combinedBuffer -> {
                            try {
                                byte[] contentCopy = new byte[combinedBuffer.readableByteCount()];
                                combinedBuffer.read(contentCopy);

                                // 使用 exchange 获取 BufferFactory
                                DataBuffer newBuffer = exchange.getResponse().bufferFactory().wrap(contentCopy);
                                return Flux.just(newBuffer);
                            } catch (Exception e) {
                                log.error("Failed to read request body", e);
                                return Flux.error(new RuntimeException("Body read failed", e));
                            } finally {
                                DataBufferUtils.release(combinedBuffer);
                            }
                        })
                        .switchIfEmpty(Flux.defer(() -> {
                            // 使用 exchange 获取 BufferFactory
                            return Flux.just(exchange.getResponse().bufferFactory().wrap(new byte[0]));
                        }));
            }
        };
    }

    // 打印带 Body 的请求日志
    private Mono<Void> logRequestWithBody(ServerHttpRequest request) {
        if (request instanceof ServerHttpRequestDecorator) {
            return request.getBody()
                    .reduce(DataBuffer::write)
                    .map(buffer -> {
                        try {
                            byte[] bytes = new byte[buffer.readableByteCount()];
                            buffer.read(bytes); // 读取字节内容
                            return bytes;
                        } finally {
                            DataBufferUtils.release(buffer); // 确保资源释放
                        }
                    })
                    .defaultIfEmpty(new byte[0]) // 如果没有数据，返回空字节数组
                    .doOnNext(allBytes -> {
                        String bodyStr = allBytes.length > 0 ? new String(allBytes, StandardCharsets.UTF_8) : "";

                        log.info("Request Start | Path: {} | Method: {} | Headers: {} | body:{}",
                                request.getPath(),
                                request.getMethod(),
                                request.getHeaders(),
                                sanitizeSensitiveInfo(bodyStr));
                    })
                    .then();
        }
        return Mono.empty();
    }

    // 对敏感信息进行脱敏处理
    private String sanitizeSensitiveInfo(String body) {
        // 示例：移除或替换敏感字段（如 password、token 等）
        return body.replaceAll("(\"password\":\")([^\\\"]*)", "$1******")
                .replaceAll("(\"token\":\")([^\\\"]*)", "$1******");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // 最高优先级
    }
}