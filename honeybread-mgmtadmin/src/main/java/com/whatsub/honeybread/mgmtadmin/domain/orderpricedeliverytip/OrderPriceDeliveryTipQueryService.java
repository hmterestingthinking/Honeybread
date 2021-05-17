package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipRepository;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderPriceDeliveryTipQueryService {

    private final OrderPriceDeliveryTipRepository repository;

    public List<OrderPriceDeliveryTipResponse> getAllByStoreId(final long storeId) {
        return repository.findByStoreId(storeId).stream()
                .map(OrderPriceDeliveryTipResponse::of)
                .collect(toList());
    }

    public OrderPriceDeliveryTipResponse getTipByOrderPrice(final long storeId, final Money orderPrice) {
        return OrderPriceDeliveryTipResponse.of(
            repository.getTipByOrderPrice(storeId, orderPrice)
                .orElse(OrderPriceDeliveryTip.createZeroTip(storeId))
        );
    }

}