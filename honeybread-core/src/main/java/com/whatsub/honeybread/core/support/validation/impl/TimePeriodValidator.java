package com.whatsub.honeybread.core.support.validation.impl;

import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.support.validation.ValidTimePeriod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimePeriodValidator implements ConstraintValidator<ValidTimePeriod, TimePeriod> {

    @Override
    public void initialize(ValidTimePeriod constraintAnnotation) {

    }

    @Override
    public boolean isValid(TimePeriod period, ConstraintValidatorContext constraintValidatorContext) {
        if (period == null) {
            return false;
        }
        return period.isValid();
    }
}
