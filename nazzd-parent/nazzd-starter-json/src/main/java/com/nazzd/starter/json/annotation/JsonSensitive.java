package com.nazzd.starter.json.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nazzd.starter.json.serializer.SensitiveSerializer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * json敏感字段注解
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerializer.class)
public @interface JsonSensitive {

    /**
     * 正则表达式
     */
    String pattern();

    /**
     * 替换字符，默认为*
     */
    String replacement() default "*";

    final class Pattern {

        /**
         * 名字，保留第一个字符，例如：张*、张**
         */
        public static final String NAME = "(?<=.{1})\\S";

        /**
         * 手机号，保留前3个后4个字符，例如：130****1234
         */
        public static final String MOBILE = "(?<=\\w{3})\\w(?=\\w{4})";

        /**
         * 身份证号，保留前6个后4个字符，例如：312312****1234
         */
        public static final String ID_CARD = "(?<=\\w{6})\\w(?=\\w{4})";

        /**
         * 银行卡号，保留前6个后4个字符，例如：622222****1234
         */
        public static final String BANK_CARD = "(?<=\\w{6})\\w(?=\\w{4})";

    }

}
