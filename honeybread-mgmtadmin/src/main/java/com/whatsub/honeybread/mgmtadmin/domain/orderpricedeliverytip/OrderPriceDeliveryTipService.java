package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPriceDeliveryTipService {

    private final OrderPriceDeliveryTipRepository repository;

    public void create(final OrderPriceDeliveryTipRequest request) {
        if(repository.existsByStoreIdAndFromPriceAndToPrice(request.getStoreId(), request.getFromPrice(), request.getToPrice())) {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_ORDER_PRICE_DELIVERY_TIP);
        }
        repository.save(request.toEntity());
    }

}