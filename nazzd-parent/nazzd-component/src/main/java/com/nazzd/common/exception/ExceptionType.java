package com.nazzd.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionType {

    /**
     * 200, OK
     */
    public static final ExceptionType OK = create(200, "OK");

    /**
     * 10400, 请求消息格式错误
     */
    public static final ExceptionType HTTP_MESSAGE_NOT_READABLE = create(10400, "请求消息格式错误");

    /**
     * 10400, 参数类型不匹配
     */
    public static final ExceptionType ARGUMENT_TYPE_MISMATCH = create(10400, "参数类型不匹配");

    /**
     * 10400, 缺少Servlet请求参数
     */
    public static final ExceptionType MISSING_SERVLET_REQUEST_PARAMETER = create(10400, "缺少请求参数");

    /**
     * 10400, Servlet请求绑定异常
     */
    public static final ExceptionType SERVLET_REQUEST_BINDING_EXCEPTION = create(10400, "请求绑定异常");

    /**
     * 10400, 文件上传缺少Part
     */
    public static final ExceptionType MISSING_SERVLET_REQUEST_PART = create(10400, "文件上传缺少Part");

    /**
     * 10400, 参数校验失败, 具体异常信息可由代码返回给客户端
     */
    public static final ExceptionType BAD_REQUEST = create(10400, "参数校验失败");

    /**
     * 10404, 请求地址未找到
     */
    public static final ExceptionType NOT_FOUND = create(10404, "请求资源不存在");

    /**
     * 10405, 请求方法不支持
     */
    public static final ExceptionType REQUEST_METHOD_NOT_SUPPORTED = create(10405, "请求方法不支持");

    /**
     * 10406, 无法生成客户端接受的响应
     */
    public static final ExceptionType MEDIA_TYPE_NOT_ACCEPTABLE = create(10406, "无法生成客户端接受的响应");

    /**
     * 10415, 不支持的媒体类型
     */
    public static final ExceptionType MEDIA_TYPE_NOT_SUPPORTED = create(10415, "不支持的媒体类型");

    /**
     * 10500, 服务器异常
     */
    public static final ExceptionType SERVER_ERROR = create(10500, "服务器异常");

    /**
     * 创建静态常量
     */
    public static ExceptionType create(int code, String message) {
        return new ExceptionType(code, message);
    }

    private int code;
    private String message;

}
