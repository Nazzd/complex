package com.nazzd.starter.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.nazzd.starter.json.annotation.JsonSensitive;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;

public class SensitiveSerializer extends StdSerializer<String> implements ContextualSerializer {

    /**
     * 正则表达式
     */
    private String pattern;

    /**
     * 替换字符
     */
    private String replacement;

    public SensitiveSerializer() {
        super(String.class);
    }

    public SensitiveSerializer(String pattern, String replacement) {
        this();
        this.pattern = pattern;
        this.replacement = replacement;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // value为空或不存在正则，不处理，直接写
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(pattern)) {
            gen.writeString(value);
        }
        try {
            gen.writeString(value.replaceAll(this.pattern, this.replacement));
        } catch (Exception e) {
            // 字符替换出现异常了则直接写
            gen.writeString(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (Objects.nonNull(property)) {
            JsonSensitive annotation = property.getAnnotation(JsonSensitive.class);
            if (Objects.nonNull(annotation)) {
                return new SensitiveSerializer(annotation.pattern(), annotation.replacement());
            }
        }
        return this;
    }

}
