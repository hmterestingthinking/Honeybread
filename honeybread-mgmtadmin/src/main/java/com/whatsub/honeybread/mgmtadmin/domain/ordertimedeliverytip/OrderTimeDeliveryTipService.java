package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTip;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTipRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderTimeDeliveryTipService {

    private final OrderTimeDeliveryTipRepository repository;

    @Transactional
    public void create(final long storeId, final OrderTimeDeliveryTipRequest request) {
        if(repository.existsByStoreId(storeId)) {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_ORDER_TIME_DELIVERY_TIP);
        }
        final OrderTimeDeliveryTip orderTimeDeliveryTip = request.toEntity(storeId);
        repository.save(orderTimeDeliveryTip);
    }

    @Transactional
    public void remove(final long storeId) {
        repository.delete(findByStoreId(storeId));
    }

    private OrderTimeDeliveryTip findByStoreId(final long storeId) {
        return repository.findByStoreId(storeId)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.ORDER_TIME_DELIVERY_TIP_NOT_FOUND));
    }
}
