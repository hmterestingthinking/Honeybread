package com.whatsub.honeybread.mgmtadmin.domain.store;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "validation.product")
public class ProductValidationProperties {

    private final int maxCategoryCnt;

}