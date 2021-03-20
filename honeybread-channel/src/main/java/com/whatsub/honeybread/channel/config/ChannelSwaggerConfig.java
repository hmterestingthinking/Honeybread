package com.whatsub.honeybread.channel.config;

import com.whatsub.honeybread.channel.support.ChannelSwaggerTags;
import com.whatsub.honeybread.common.config.SwaggerConfig;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

@Configuration
public class ChannelSwaggerConfig extends SwaggerConfig {

    @Override
    public Field[] tags() {
        return ChannelSwaggerTags.class.getDeclaredFields();
    }
}
