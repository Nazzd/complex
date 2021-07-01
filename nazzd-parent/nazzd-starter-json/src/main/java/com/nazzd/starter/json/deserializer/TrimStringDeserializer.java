package com.nazzd.starter.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Objects;

@JsonComponent
public class TrimStringDeserializer extends StringDeserializer {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String result = super.deserialize(parser, context);
        if (!Objects.isNull(result)) {
            return result.trim();
        }
        return null;
    }

}
