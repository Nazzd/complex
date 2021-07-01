package com.nazzd.starter.web;

import com.nazzd.common.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异常响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private Integer code;

    private String message;

    public static ExceptionResponse fail(ExceptionType exceptionType) {
        return fail(exceptionType.getCode(), exceptionType.getMessage());
    }

    public static ExceptionResponse fail(Integer code, String message) {
        return ExceptionResponse.builder().code(code).message(message).build();
    }

}
