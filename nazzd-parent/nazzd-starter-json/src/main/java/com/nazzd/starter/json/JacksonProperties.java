package com.nazzd.starter.json;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = JacksonProperties.PREFIX)
public class JacksonProperties {

    public static final String PREFIX = "dpc.jackson";

    /**
     * 序列化属性
     */
    private Serialization serialization;

    /**
     * 反序列化属性
     */
    private Deserialization deserialization;

    @Data
    public static final class Serialization {

        /**
         * 超出js精度的数字类型转为String
         */
        private boolean bigNumberToString = true;

    }

    @Data
    public static final class Deserialization {

        /**
         * 字符串trim
         */
        private boolean trimString = true;

    }


}
