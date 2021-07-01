package com.nazzd.starter.web;

import com.nazzd.common.exception.ExceptionType;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.TypeMismatchException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

public class ExceptionMessageUtils {

    public static String getHttpRequestMethodNotSupportedExceptionMessage(HttpRequestMethodNotSupportedException ex) {
        return String.format("请求方法'%s'不支持, 支持的请求方法: %s",
                ex.getMethod(), Arrays.toString(ex.getSupportedMethods()));
    }

    public static String getHttpMediaTypeNotSupportedExceptionMessage(HttpMediaTypeNotSupportedException ex) {
        return String.format("媒体类型'%s'不支持, 支持的媒体类型: %s",
                ex.getContentType(), Arrays.toString(ex.getSupportedMediaTypes().toArray()));
    }

    /**
     * 缺少@PathVariable
     */
    public static String getMissingPathVariableExceptionMessage(MissingPathVariableException ex) {
        return String.format("缺少路径参数: %s", ex.getVariableName());
    }

    /**
     * 缺少@RequestParam
     */
    public static String getMissingServletRequestParameterExceptionMessage(MissingServletRequestParameterException ex) {
        return String.format("缺少请求参数: %s", ex.getParameterName());
    }

    public static String getTypeMismatchExceptionMessage(TypeMismatchException ex) {
        return String.format("%s参数类型不匹配", ex.getPropertyName());
    }

    /**
     * RequestParam或PathVariable没有通过校验规则
     */
    public static String getConstraintViolationMessage(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        return Optional.ofNullable(constraintViolations)
                .map(Set::iterator)
                .filter(Iterator::hasNext)
                .map(Iterator::next)
                .map(violation -> getBadRequestMessage(((PathImpl) violation.getPropertyPath()).getLeafNode().getName(), violation.getMessage()))
                .orElse(ExceptionType.BAD_REQUEST.getMessage());
    }

    /**
     * RequestBody或者Form表单没有通过校验规则
     */
    public static String getFieldErrorMessage(List<FieldError> fieldErrors) {
        return Optional.ofNullable(fieldErrors)
                .filter(errors -> !CollectionUtils.isEmpty(errors))
                .map(errors -> errors.get(0))
                .map(fieldError -> getBadRequestMessage(fieldError.getField(), fieldError.getDefaultMessage()))
                .orElse(ExceptionType.BAD_REQUEST.getMessage());
    }

    /**
     * 根据字段名和校验结果获取最后的异常信息, 主要用于RequestBody, Form表单, RequestParam, PathVariable
     */
    private static String getBadRequestMessage(String field, String error) {
        return field + ": " + error;
    }

}
