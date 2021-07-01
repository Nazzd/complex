package com.nazzd.common.exception;

/**
 * 服务端异常，当catch到其它异常抛出此异常时，需要调用带cause的三个参数的方法传入异常链信息，日志级别为error
 */
public class ServerException extends BaseException {

    public ServerException(String reason) {
        this(ExceptionType.SERVER_ERROR.getCode(), reason);
    }

    public ServerException(String reason, Throwable cause) {
        this(ExceptionType.SERVER_ERROR.getCode(), reason, cause);
    }

    public ServerException(ExceptionType exceptionType) {
        this(exceptionType.getCode(), exceptionType.getMessage());
    }

    public ServerException(ExceptionType exceptionType, Throwable cause) {
        this(exceptionType.getCode(), exceptionType.getMessage(), cause);
    }

    public ServerException(int code, String reason) {
        super(code, reason);
    }

    public ServerException(int code, String reason, Throwable cause) {
        super(code, reason, cause);
    }

}
