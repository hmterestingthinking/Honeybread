package com.whatsub.honeybread.mgmtadmin.config;

import com.whatsub.honeybread.common.config.WebMvcConfig;
import com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite.ChannelUserSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class MgmtAdminMvcConfig extends WebMvcConfig {

    @Bean
    @RequestScope
    public ChannelUserSession channelUserSession() {
        return new ChannelUserSession(1L);
    }

}
