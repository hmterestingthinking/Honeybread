package com.whatsub.honeybread.core.support.validation;

import com.whatsub.honeybread.core.support.validation.impl.TimePeriodValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimePeriodValidator.class)
@Target(ElementType.FIELD)
public @interface ValidTimePeriod {

    String message() default "잘못된 기간 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
