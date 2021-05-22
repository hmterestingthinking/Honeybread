package com.whatsub.honeybread.common.config;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.whatsub.honeybread.core.infra.serializer.LocalDateTimeDeserializer;
import com.whatsub.honeybread.core.infra.serializer.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.TimeZone;

public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .addModules(
                new ParameterNamesModule(),
                new Jdk8Module(),
                new JavaTimeModule(),
                getLocalDateTimeModule()
            )
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
            .enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS)
            .defaultTimeZone(TimeZone.getDefault())
            .defaultLocale(Locale.getDefault())
            .build();
    }

    private Module getLocalDateTimeModule() {
        final SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        return module;
    }
}
