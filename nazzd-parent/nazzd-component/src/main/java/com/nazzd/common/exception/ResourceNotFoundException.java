package com.nazzd.common.exception;

/**
 * 请求资源不存在异常，返回404错误
 */
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String reason) {
        super(ExceptionType.NOT_FOUND.getCode(), reason);
    }

    public ResourceNotFoundException(String reason, Throwable cause) {
        super(ExceptionType.NOT_FOUND.getCode(), reason, cause);
    }

}
