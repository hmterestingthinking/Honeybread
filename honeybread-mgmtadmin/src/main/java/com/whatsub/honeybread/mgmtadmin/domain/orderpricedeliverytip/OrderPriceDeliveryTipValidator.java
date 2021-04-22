package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

@Component
public class OrderPriceDeliveryTipValidator {

    public void validate(OrderPriceDeliveryTip entity) {
        final BeanPropertyBindingResult errors = new BeanPropertyBindingResult(entity, entity.getClass().getSimpleName());
        validatePrices(errors, entity.getFromPrice(), entity.getToPrice());
        if(errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

    private void validatePrices(final BeanPropertyBindingResult errors, final Money fromPrice, final Money toPrice) {
        if(fromPrice.isGreaterThan(toPrice)) {
            errors.reject("invalid.price.criteria", "fromPrice값은 toPrice값보다 작아야합니다.");
        }
    }
}
