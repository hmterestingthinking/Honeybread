package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipRepository;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipValidator;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderPriceDeliveryTipService {

    private final OrderPriceDeliveryTipRepository repository;
    private final OrderPriceDeliveryTipValidator validator;

    @Transactional
    public void create(final Long storeId, final OrderPriceDeliveryTipRequest request) {
        if(repository.existsByStoreIdAndFromPriceAndToPrice(storeId,
                                                            request.getFromPrice(),
                                                            request.getToPrice())) {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_ORDER_PRICE_DELIVERY_TIP);
        }
        final OrderPriceDeliveryTip entity = request.toEntity(storeId);
        validator.validate(entity);
        checkPriceRange(storeId, request);
        repository.save(entity);
    }

    @Transactional
    public void delete(final Long id) {
        repository.delete(findById(id));
    }

    private OrderPriceDeliveryTip findById(final Long id) {
        return repository.findById(id).orElseThrow(
                () -> new HoneyBreadException(ErrorCode.ORDER_PRICE_DELIVERY_TIP_NOT_FOUND));
    }

    private void checkPriceRange(final Long storeId, final OrderPriceDeliveryTipRequest request) {
        if(repository.existsByStoreIdAndToPriceIsNull(storeId)
            || repository.getTipByOrderPrice(storeId, request.getFromPrice()).isPresent()
            || repository.getTipByOrderPrice(storeId, request.getToPrice()).isPresent()) {
            throw new HoneyBreadException(ErrorCode.PRICE_RANGE_ALREADY_EXISTS);
        }
    }

}