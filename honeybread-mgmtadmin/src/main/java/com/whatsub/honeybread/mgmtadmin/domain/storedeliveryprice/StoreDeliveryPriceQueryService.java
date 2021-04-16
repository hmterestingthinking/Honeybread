package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPriceRepository;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreDeliveryPriceQueryService {

    private final StoreDeliveryPriceRepository repository;

    public Map<Integer, List<StoreDeliveryPriceResponse>> getStoreDeliveryPrices(final Long storeId) {
        return repository.getStoreDeliveryPrices(storeId).entrySet().stream()
            .collect(Collectors.toMap(
                e -> e.getKey().getValue().intValue(),
                e -> e.getValue().stream().map(StoreDeliveryPriceResponse::of).collect(toList())
            ));
    }

}
