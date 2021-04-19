package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPriceRepository;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceGroupResponse;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceGroupView;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreDeliveryPriceQueryService {

    private final StoreDeliveryPriceRepository repository;

    public StoreDeliveryPriceGroupView getStoreDeliveryPrices(final Long storeId) {
        final List<StoreDeliveryPrice> storeDeliveryPrices = repository.getStoreDeliveryPrices(storeId);
        List<StoreDeliveryPriceGroupResponse> response = storeDeliveryPrices.stream()
            .collect(Collectors.groupingBy(
                storeDeliveryPrice -> storeDeliveryPrice.getPrice().getValue().intValue(),
                mapping(StoreDeliveryPriceResponse::of, toList())
            )).entrySet().stream()
            .map(e -> new StoreDeliveryPriceGroupResponse(e.getKey(), e.getValue()))
            .collect(toList());

        return new StoreDeliveryPriceGroupView(response);
    }

}
