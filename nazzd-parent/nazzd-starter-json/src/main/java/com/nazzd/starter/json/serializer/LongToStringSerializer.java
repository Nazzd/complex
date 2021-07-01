package com.nazzd.starter.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class LongToStringSerializer extends JsonSerializer<Long> {

    private static final Long JS_MAX_NUMBER = 9007199254740991L;

    public static final LongToStringSerializer INSTANCE = new LongToStringSerializer();

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 大于js的最大精度
        if (value.compareTo(JS_MAX_NUMBER) > 0) {
            gen.writeString(value.toString());
        } else {
            gen.writeNumber(value);
        }
    }

}
