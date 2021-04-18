package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;


import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceModifyRequest;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceRequest;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("stores/delivery-prices")
@RequiredArgsConstructor
public class StoreDeliveryPriceController {

    private final StoreDeliveryPriceService service;
    private final StoreDeliveryPriceQueryService queryService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody StoreDeliveryPriceRequest request, BindingResult result) {
        if(result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{storeId}")
    public ResponseEntity<Void> update(@PathVariable("storeId") Long storeId,
                                       @Valid @RequestBody StoreDeliveryPriceModifyRequest request,
                                       BindingResult result) {
        if(result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.update(storeId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{storeId}")
    public ResponseEntity<Void> delete(@PathVariable("storeId") Long storeId) {
        service.delete(storeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{storeId}")
    public ResponseEntity<Map<Integer, List<StoreDeliveryPriceResponse>>> get(@PathVariable("storeId") Long storeId) {
        final Map<Integer, List<StoreDeliveryPriceResponse>> storeDeliveryPrices
            = queryService.getStoreDeliveryPrices(storeId);
        return ResponseEntity.ok().body(storeDeliveryPrices);
    }
}
