package com.whatsub.honeybread.core.support.validation.impl;


import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.support.validation.ValidTimePeriod;
import lombok.Value;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class TimePeriodValidatorTest {
    static ValidatorFactory validatorFactory;
    static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void clean() {
        validatorFactory.close();
    }

    @Value
    class Request {
        @ValidTimePeriod
        TimePeriod period;
    }

    @Test
    void 시작일과_종료일이_같다면_실패() throws Exception {
        // given
        LocalDateTime from = LocalDateTime.now();
        TimePeriod timePeriod = TimePeriod.of(
            from, from.plusDays(0)
        );

        Request request = new Request(timePeriod);

        // when
        Set<ConstraintViolation<Request>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void 시작일이_종료일보다_늦다면_실패() throws Exception {
        // given
        LocalDateTime from = LocalDateTime.now();
        TimePeriod timePeriod = TimePeriod.of(
            from, from.minusDays(10)
        );

        Request request = new Request(timePeriod);

        // when
        Set<ConstraintViolation<Request>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void 시작일이_종료일보다_빠르다면_성공() throws Exception {
        // given
        LocalDateTime from = LocalDateTime.now();
        TimePeriod timePeriod = TimePeriod.of(
            from, from.plusDays(1)
        );

        Request request = new Request(timePeriod);

        // when
        Set<ConstraintViolation<Request>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }
}