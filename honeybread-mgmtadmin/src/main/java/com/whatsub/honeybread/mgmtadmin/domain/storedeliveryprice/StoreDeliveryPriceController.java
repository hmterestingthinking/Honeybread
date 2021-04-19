package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;


import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceGroupResponse;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceModifyRequest;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceRequest;
import com.whatsub.honeybread.mgmtadmin.support.MgmtAdminSwaggerTags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@RestController
@RequestMapping("stores/delivery-prices")
@RequiredArgsConstructor
@Api(tags = HoneyBreadSwaggerTags.ALL)
public class StoreDeliveryPriceController {

    private final StoreDeliveryPriceService service;
    private final StoreDeliveryPriceQueryService queryService;

    @ApiOperation(
        value = "주소별 배달금액 생성",
        tags = MgmtAdminSwaggerTags.STORE_DELIVERY_PRICE
    )
    @PostMapping
    public ResponseEntity<Void> create(final @Valid @RequestBody StoreDeliveryPriceRequest request, BindingResult result) {
        if(result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(
        value = "주소별 배달금액 수정",
        tags = MgmtAdminSwaggerTags.STORE_DELIVERY_PRICE
    )
    @PutMapping("{storeId}")
    public ResponseEntity<Void> update(final @PathVariable("storeId") Long storeId,
                                       final @Valid @RequestBody StoreDeliveryPriceModifyRequest request,
                                       final BindingResult result) {
        if(result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.update(storeId, request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(
        value = "주소별 배달금액 삭제",
        tags = MgmtAdminSwaggerTags.STORE_DELIVERY_PRICE
    )
    @DeleteMapping("{storeId}")
    public ResponseEntity<Void> delete(final @PathVariable("storeId") Long storeId) {
        service.delete(storeId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
        value = "주소별 배달금액 목록 조회",
        tags = MgmtAdminSwaggerTags.STORE_DELIVERY_PRICE
    )
    @GetMapping("{storeId}")
    public ResponseEntity<StoreDeliveryPriceGroupResponse> get(final @PathVariable("storeId") Long storeId) {
        final StoreDeliveryPriceGroupResponse storeDeliveryPrices
            = queryService.getStoreDeliveryPrices(storeId);
        return ResponseEntity.ok().body(storeDeliveryPrices);
    }
}
