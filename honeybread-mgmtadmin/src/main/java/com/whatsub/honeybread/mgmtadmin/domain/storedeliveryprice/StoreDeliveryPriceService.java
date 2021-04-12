package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPriceRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceModifyRequest;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreDeliveryPriceService {

    private final StoreDeliveryPriceRepository repository;

    @Transactional
    public void create(final StoreDeliveryPriceRequest request) {
        if(repository.existsByStoreIdAndSearchableDeliveryAddress(request.getStoreId(), request.getSearchableDeliveryAddress())) {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_STORE_DELIVERY_PRICE);
        }
        repository.save(request.toEntity());
    }

    @Transactional
    public void update(final Long id, final StoreDeliveryPriceModifyRequest request) {
        StoreDeliveryPrice storeDeliveryPrice = repository.findById(id)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.STORE_DELIVERY_PRICE_NOT_FOUND));

        StoreDeliveryPrice modifyEntity = request.toEntity();
        storeDeliveryPrice.update(modifyEntity);
    }
}
