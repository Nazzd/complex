package com.nazzd.starter.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 * (code,data,message)的统一结果封装
 */
@ControllerAdvice(annotations = {RestController.class})
public class RestResponseBodyAdvice implements ResponseBodyAdvice {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 被异常处理器处理过的结果, 不再处理, 直接返回
        if (body instanceof RestResponse) {
            return body;
        }
        // 针对Controller方法返回String结果需要特殊处理, 建议不要在Controller中返回String
        else if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(RestResponse.success(body));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        // 成功情况
        return RestResponse.success(body);
    }

}
