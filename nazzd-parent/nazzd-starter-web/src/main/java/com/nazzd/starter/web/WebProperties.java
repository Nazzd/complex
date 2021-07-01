package com.nazzd.starter.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = WebProperties.PREFIX)
public class WebProperties {

    public static final String PREFIX = "zf.dpc.web";

    /**
     * 是否使用responseBodyAdvice, 默认不使用, 直接返回响应结果
     */
    private boolean responseBodyAdvice = false;

    /**
     * 对字符串请求参数进行trim
     */
    private boolean trimString = true;

}
