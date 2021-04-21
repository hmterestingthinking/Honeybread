package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipRepository;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

}
