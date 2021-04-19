package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPriceRepository;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceGroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreDeliveryPriceQueryService {

    private final StoreDeliveryPriceRepository repository;

    public StoreDeliveryPriceGroupResponse getStoreDeliveryPrices(final Long storeId) {
        final List<StoreDeliveryPrice> storeDeliveryPrices = repository.getStoreDeliveryPrices(storeId);
        return StoreDeliveryPriceGroupResponse.of(storeDeliveryPrices);
    }

}
