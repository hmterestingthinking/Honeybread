package com.whatsub.honeybread.core.domain.store.validator;

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