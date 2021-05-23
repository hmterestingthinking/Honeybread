package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.infra.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class OrderTimeDeliveryTipValidator {

    private static final LocalTime EIGHT_PM = LocalTime.of(20, 00);
    private static final LocalTime NINE_AM = LocalTime.of(9, 00);
    private OrderTimeDeliveryTipRepository repository;

    public void validate(OrderTimeDeliveryTip entity) {
        final BeanPropertyBindingResult errors = new BeanPropertyBindingResult(entity, entity.getClass().getSimpleName());
        validateTime(errors, entity.getDeliveryTimePeriod().getFromTime());
        validateTime(errors, entity.getDeliveryTimePeriod().getToTime());
        validateTimeRange(errors, entity.getDeliveryTimePeriod().getFromTime(), entity.getDeliveryTimePeriod().getToTime());
        if(errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

    private void validateTime(final BeanPropertyBindingResult errors, final LocalTime time) {
        final int minuteByMidnight = DeliveryTimePeriodUtil.convertMinuteByMidnight(time);
        if(minuteByMidnight < DeliveryTimePeriodUtil.convertMinuteByMidnight(EIGHT_PM)
            || minuteByMidnight >= DeliveryTimePeriodUtil.convertMinuteByMidnight(NINE_AM)) {
            errors.reject("invalid.time", "시간값은 20시 이상, 9시 미만이어야합니다.");
        }
    }

    private void validateTimeRange(final BeanPropertyBindingResult errors,
                                   final LocalTime fromTime,
                                   final LocalTime toTime) {
        final int fromMinuteByMidnight = DeliveryTimePeriodUtil.convertMinuteByMidnight(fromTime);
        final int toMinuteByMidnight = DeliveryTimePeriodUtil.convertMinuteByMidnight(toTime);
        if(fromMinuteByMidnight >= toMinuteByMidnight) {
            errors.reject("invalid.time.range", "fromTime은 toTime보다 빠른 시간이어야 합니다.");
        }
    }
}
