package com.nazzd.common.exception;

/**
 * 客户端异常, 日志级别为warn
 */
public class ClientException extends BaseException {

    public ClientException(String reason) {
        this(ExceptionType.BAD_REQUEST.getCode(), reason);
    }

    public ClientException(String reason, Throwable cause) {
        this(ExceptionType.BAD_REQUEST.getCode(), reason, cause);
    }

    public ClientException(ExceptionType exceptionType) {
        this(exceptionType.getCode(), exceptionType.getMessage());
    }

    public ClientException(ExceptionType exceptionType, Throwable cause) {
        this(exceptionType.getCode(), exceptionType.getMessage(), cause);
    }

    public ClientException(int code, String reason) {
        super(code, reason);
    }

    public ClientException(int code, String reason, Throwable cause) {
        super(code, reason, cause);
    }

}
