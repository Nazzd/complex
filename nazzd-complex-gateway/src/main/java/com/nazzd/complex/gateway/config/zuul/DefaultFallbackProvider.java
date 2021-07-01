package com.nazzd.complex.gateway.config.zuul;

import com.netflix.client.ClientException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 所有路由默认FallbackProvider, 主要增加了503和504错误的处理
 */
@Component
public class DefaultFallbackProvider implements FallbackProvider {

    @Override
    public String getRoute() {
        // 针对所有路由
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        // 服务不可用503, ClientException是找不到服务, HttpHostConnectException是在服务刚停止的时候会出现
        if (cause instanceof ClientException || cause instanceof HttpHostConnectException) {
            return response(HttpStatus.SERVICE_UNAVAILABLE);
        }
        // 超时504
        else if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        }
        // 其他异常500
        else {
            return response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {

            @NonNull
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }

            @NonNull
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream("fallback".getBytes(StandardCharsets.UTF_8));
            }

            @NonNull
            @Override
            public HttpStatus getStatusCode() {
                return status;
            }

            @Override
            public int getRawStatusCode() {
                return status.value();
            }

            @NonNull
            @Override
            public String getStatusText() {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {
                // 默认
            }

        };
    }

}
