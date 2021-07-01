package com.nazzd.starter.web;


import com.nazzd.common.exception.ClientException;
import com.nazzd.common.exception.ExceptionType;
import com.nazzd.common.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * (code,data,message)统一异常处理(没有使用响应吗)
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * ClientException, 日志warn级别
     */
    @ExceptionHandler(ClientException.class)
    public RestResponse handleClientException(ClientException ex) {
        log.warn(ex.getMessage());
        return RestResponse.fail(ex.getCode(), ex.getReason());
    }

    /**
     * ServerException, 日志error级别
     */
    @ExceptionHandler(ServerException.class)
    public RestResponse handleServerException(ServerException ex) {
        log.error(ex.getMessage(), ex);
        return RestResponse.fail(ex.getCode(), ex.getReason());
    }

    /**
     * Exception, 日志error级别
     */
    @ExceptionHandler(Exception.class)
    public RestResponse handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return RestResponse.fail(ExceptionType.SERVER_ERROR);
    }

    /**
     * RequestParam或PathVariable没有通过校验规则
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public RestResponse handleConstraintViolation(ConstraintViolationException ex) {
        String message = ExceptionMessageUtils.getConstraintViolationMessage(ex);
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.BAD_REQUEST.getCode(), message));
    }

    /**
     * RequestBody没有通过校验规则
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected RestResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ExceptionMessageUtils.getFieldErrorMessage(ex.getBindingResult().getFieldErrors());
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.BAD_REQUEST.getCode(), message));
    }

    /**
     * Form表单提交没有通过校验规则
     */
    @ExceptionHandler(BindException.class)
    protected RestResponse handleBindException(BindException ex) {
        String message = ExceptionMessageUtils.getFieldErrorMessage(ex.getFieldErrors());
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.BAD_REQUEST.getCode(), message));
    }

    /**
     * 请求方法不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected RestResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn(ex.getMessage());
        String message = ExceptionMessageUtils.getHttpRequestMethodNotSupportedExceptionMessage(ex);
        return this.handleExceptionInternal(ex,
                RestResponse.fail(ExceptionType.REQUEST_METHOD_NOT_SUPPORTED.getCode(), message));
    }

    /**
     * 不支持的媒体类型
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected RestResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        String message = ExceptionMessageUtils.getHttpMediaTypeNotSupportedExceptionMessage(ex);
        return this.handleExceptionInternal(ex,
                RestResponse.fail(ExceptionType.MEDIA_TYPE_NOT_SUPPORTED.getCode(), message));
    }

    /**
     * 无法生成客户端接受的响应
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    protected RestResponse handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.MEDIA_TYPE_NOT_ACCEPTABLE));
    }

    /**
     * 缺少路径参数
     */
    @ExceptionHandler(MissingPathVariableException.class)
    protected RestResponse handleMissingPathVariableException(MissingPathVariableException ex) {
        String message = ExceptionMessageUtils.getMissingPathVariableExceptionMessage(ex);
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.BAD_REQUEST.getCode(), message));
    }

    /**
     * 缺少请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected RestResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String message = ExceptionMessageUtils.getMissingServletRequestParameterExceptionMessage(ex);
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.BAD_REQUEST.getCode(), message));
    }

    /**
     * 请求绑定异常
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    protected RestResponse handleServletRequestBindingException(ServletRequestBindingException ex) {
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.SERVLET_REQUEST_BINDING_EXCEPTION));
    }

    /**
     * 文件上传缺少Part
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    protected RestResponse handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.MISSING_SERVLET_REQUEST_PART));
    }

    /**
     * 参数类型不匹配: Bean属性转换不支持
     */
    @ExceptionHandler(ConversionNotSupportedException.class)
    protected RestResponse handleConversionNotSupportedException(ConversionNotSupportedException ex) {
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.ARGUMENT_TYPE_MISMATCH));
    }

    /**
     * 参数类型不匹配: Bean属性类型不匹配
     */
    @ExceptionHandler(TypeMismatchException.class)
    protected RestResponse handleTypeMismatchException(TypeMismatchException ex) {
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.ARGUMENT_TYPE_MISMATCH));
    }

    /**
     * 请求消息格式错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected RestResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.HTTP_MESSAGE_NOT_READABLE));
    }

    /**
     * 响应消息写入失败
     */
    @ExceptionHandler(HttpMessageNotWritableException.class)
    protected RestResponse handleHttpMessageNotWritableException(HttpMessageNotWritableException ex) {
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.SERVER_ERROR));
    }

    /**
     * 没有找到处理器
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected RestResponse handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return this.handleExceptionInternal(ex, RestResponse.fail(ExceptionType.NOT_FOUND));
    }

    private RestResponse handleExceptionInternal(Exception ex, RestResponse body) {
        log.warn(ex.getMessage());
        return body;
    }

}
