package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

@Component
@RequiredArgsConstructor
public class OrderPriceDeliveryTipValidator {

    private final OrderPriceDeliveryTipRepository repository;

    public void validate(OrderPriceDeliveryTip entity) {
        final BeanPropertyBindingResult errors = new BeanPropertyBindingResult(entity, entity.getClass().getSimpleName());
        validatePrices(errors, entity.getFromPrice(), entity.getToPrice());
        validateDuplicatePriceRange(errors, entity.getStoreId(), entity.getFromPrice(), entity.getToPrice());
        if(errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

    private void validateDuplicatePriceRange(final BeanPropertyBindingResult errors,
                                             final Long storeId,
                                             final Money fromPrice,
                                             final Money toPrice) {
        if(toPrice == null && repository.existsByStoreIdAndToPriceIsNull(storeId)
            || repository.getTipByOrderPrice(storeId, fromPrice).isPresent()
            || (toPrice != null && repository.getTipByOrderPrice(storeId, toPrice).isPresent())) {
            errors.reject("duplicate.price.range", "이미존재하는 가격범위가 있습니다.");
        }
    }

    private void validatePrices(final BeanPropertyBindingResult errors, final Money fromPrice, final Money toPrice) {
        if(toPrice != null && fromPrice.isGreaterThan(toPrice)) {
            errors.reject("invalid.price.range", "fromPrice값은 toPrice값보다 작아야합니다.");
        }
    }
}
