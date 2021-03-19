package com.whatsub.honeybread.core.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Money implements Serializable {
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private BigDecimal value;

    protected Money() {
        this.value = BigDecimal.ZERO;
    }

    private Money(BigDecimal value) {
        this.value = BigDecimal.valueOf(value.longValue());
    }

    public static Money wons(Integer value) {
        if (value == null || value <= 0) {
            return Money.ZERO;
        }
        return wons(value.longValue());
    }

    public static Money wons(long value) {
        if (value <= 0) {
            return Money.ZERO;
        }
        return new Money(BigDecimal.valueOf(value));
    }

    public BigDecimal getValue() {
        return BigDecimal.valueOf(this.value.longValue());
    }
}
