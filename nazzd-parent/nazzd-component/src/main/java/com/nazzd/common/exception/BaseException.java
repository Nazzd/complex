package com.nazzd.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BaseException
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误原因
     */
    private final String reason;

    public BaseException(int code, String reason) {
        super(reason);
        this.code = code;
        this.reason = reason;
    }

    public BaseException(int code, String reason, Throwable cause) {
        super(reason, cause);
        this.code = code;
        this.reason = reason;
    }

}
