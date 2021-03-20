package com.whatsub.honeybread.mgmtadmin.config;

import com.whatsub.honeybread.common.config.SwaggerConfig;
import com.whatsub.honeybread.mgmtadmin.support.MgmtAdminSwaggerTags;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

@Configuration
public class MgmtAdminSwaggerConfig extends SwaggerConfig {

    @Override
    public Field[] tags() {
        return MgmtAdminSwaggerTags.class.getDeclaredFields();
    }
}
