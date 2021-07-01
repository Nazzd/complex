package com.nazzd.starter.web;

import com.nazzd.common.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (code,data,message)包装返回类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {

    private Integer code;

    private String message;

    private T data;

    public static RestResponse success(Object data) {
        return RestResponse.builder().code(ExceptionType.OK.getCode())
                .message(ExceptionType.OK.getMessage()).data(data).build();
    }

    public static RestResponse fail(ExceptionType exceptionType) {
        return fail(exceptionType.getCode(), exceptionType.getMessage());
    }

    public static RestResponse fail(Integer code, String message) {
        return RestResponse.builder().code(code).message(message).build();
    }

}
