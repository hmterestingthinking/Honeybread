package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTip;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTipRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderTimeDeliveryTipQueryService {

    private final OrderTimeDeliveryTipRepository repository;

    public OrderTimeDeliveryTipResponse getTipByTime(final long storeId, final LocalTime time, final DayOfWeek dayOfWeek) {
        return OrderTimeDeliveryTipResponse.of(
            repository.getTipByTime(storeId, time, dayOfWeek)
                .orElse(OrderTimeDeliveryTip.createZeroTip(storeId))
        );
    }

    public OrderTimeDeliveryTipResponse getTipByStoreId(final long storeId) {
        return OrderTimeDeliveryTipResponse.of(
            repository.findByStoreId(storeId).orElseThrow(
                () -> new HoneyBreadException(ErrorCode.ORDER_TIME_DELIVERY_TIP_NOT_FOUND)
            )
        );
    }

}
