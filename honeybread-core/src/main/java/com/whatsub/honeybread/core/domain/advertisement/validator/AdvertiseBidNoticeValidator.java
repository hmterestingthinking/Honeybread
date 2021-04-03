package com.whatsub.honeybread.core.domain.advertisement.validator;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.temporal.ChronoUnit;

@Component
public class AdvertiseBidNoticeValidator {
    private static final int MINIMUM_BID_NOTICE_DAYS = 30;
    private static final int MAXIMUM_STORE_COUNTS = 50;
    private static final Money MINIMUM_BID_PRICE_UNIT = Money.wons(10_000);
    private static final Money BID_PRICE_UNIT = Money.wons(10_000);

    public void validate(AdvertisementBidNotice entity) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(entity, entity.getClass().getSimpleName());

        validateMaximumStore(entity.getMaximumStoreCounts(), errors);
        validateBidPriceUnit(entity.getBidPriceUnit(), errors);
        validatePeriod(entity.getPeriod(), errors);

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

    /**
     * 낙찰 가능한 최대 스토어의 수는 최소 50 이어야 한다.
     */
    private void validateMaximumStore(int maximumStoreCounts, BeanPropertyBindingResult errors) {
        if (maximumStoreCounts < MAXIMUM_STORE_COUNTS) {
            errors.rejectValue(
                "maximumStoreCounts",
                "advertise.bid.maximums_store_counts",
                new Object[]{"낙찰 가능한 최대 스토어"},
                String.format("낙찰 가능한 최대 스토어의 수는 최소 %s 이상 이어야 합니다.", MAXIMUM_STORE_COUNTS)
            );
        }
    }

    /**
     * 입찰 단위는 최소 10_000 원 이어야 한다.
     * 입찰 단위는 10_000 원 단위어야 한다.
     */
    private void validateBidPriceUnit(Money bidPriceUnit, BeanPropertyBindingResult errors) {
        if (bidPriceUnit.isLessThan(MINIMUM_BID_PRICE_UNIT)) {
            errors.rejectValue(
                "bidPriceUnit",
                "advertise.bid_price_unit.minimum.value",
                new Object[]{"입찰 단위"},
                String.format("입찰 단위는 최소 %s 이상 이어야 합니다.", MINIMUM_BID_PRICE_UNIT)
            );
        }

        if (!bidPriceUnit.isUnitOf(BID_PRICE_UNIT)) {
            errors.rejectValue(
                "bidPriceUnit",
                "advertise.bid_price_unit",
                new Object[]{"입찰 단위"},
                String.format("입찰 단위는 %s 이어야 합니다.", BID_PRICE_UNIT)
            );
        }
    }

    /**
     * 광고기간은 최소 30일 이어야 한다.
     */
    private void validatePeriod(TimePeriod period, Errors errors) {
        if (ChronoUnit.DAYS.between(period.getFrom(), period.getTo()) < MINIMUM_BID_NOTICE_DAYS) {
            errors.rejectValue(
                "period",
                "advertise.bid.notice.period.minimum.days",
                new Object[]{"광고 기간"},
                String.format("최소 광고 기간은 %s 이상 이어야 합니다.", MINIMUM_BID_NOTICE_DAYS)
            );
        }
    }
}
