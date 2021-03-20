package com.whatsub.honeybread.common.config;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;
import java.util.TimeZone;

public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .addModules(new ParameterNamesModule(), new Jdk8Module(), new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS)
            .defaultTimeZone(TimeZone.getDefault())
            .defaultLocale(Locale.getDefault())
            .build();
    }
}
