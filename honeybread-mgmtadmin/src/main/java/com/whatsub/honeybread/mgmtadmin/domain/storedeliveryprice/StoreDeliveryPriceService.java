package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPriceRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreDeliveryPriceService {

    private final StoreDeliveryPriceRepository repository;

    public void create(final StoreDeliveryPriceRequest request) {
        if(repository.existsByStoreIdAndSearchableDeliveryAddress(request.getStoreId(), request.getSearchableDeliveryAddress())) {
            throw new HoneyBreadException(ErrorCode.STORE_DELIVERY_PRICE_NOT_FOUND);
        }
        repository.save(request.toEntity());
    }
}
