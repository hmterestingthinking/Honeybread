package com.whatsub.honeybread.core.infra.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("UTC");

    public LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(
        final JsonParser jsonParser,
        final DeserializationContext deserializationContext
    ) throws IOException, JsonProcessingException {
        final long value = jsonParser.getValueAsLong();
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), DEFAULT_ZONE_ID);
    }
}
