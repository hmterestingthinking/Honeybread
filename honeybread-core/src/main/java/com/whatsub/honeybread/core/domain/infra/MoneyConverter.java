package com.whatsub.honeybread.core.domain.infra;

import com.whatsub.honeybread.core.domain.model.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, Long> {
    @Override
    public Long convertToDatabaseColumn(final Money money) {
        return money.getValue().longValue();
    }

    @Override
    public Money convertToEntityAttribute(final Long value) {
        return Money.wons(value);
    }
}
