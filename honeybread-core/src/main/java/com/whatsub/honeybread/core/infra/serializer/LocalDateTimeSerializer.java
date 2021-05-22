package com.whatsub.honeybread.core.infra.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("UTC");

    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(
        final LocalDateTime value,
        final JsonGenerator jsonGenerator,
        final SerializerProvider serializerProvider
    ) throws IOException {
        if (value != null) {
            long millis = value.atZone(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
            jsonGenerator.writeNumber(millis);
        } else {
            jsonGenerator.writeNull();
        }
    }
}
