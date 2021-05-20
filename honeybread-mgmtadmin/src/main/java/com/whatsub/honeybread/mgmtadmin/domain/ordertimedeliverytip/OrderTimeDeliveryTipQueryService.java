package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTip;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTipRepository;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderTimeDeliveryTipQueryService {

    private final OrderTimeDeliveryTipRepository repository;

    public OrderTimeDeliveryTipResponse getTipByTime(final long storeId, final LocalTime time) {
        return OrderTimeDeliveryTipResponse.of(
            repository.getTipByTime(storeId, time)
                .orElse(OrderTimeDeliveryTip.createZeroTip(storeId))
        );
    }

}
