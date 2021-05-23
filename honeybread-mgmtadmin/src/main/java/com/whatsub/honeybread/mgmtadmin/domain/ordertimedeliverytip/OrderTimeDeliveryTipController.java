package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipRequest;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipResponse;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipSearch;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RestController
@RequiredArgsConstructor
@RequestMapping("stores/{storeId}/order-time-delivery-tips")
public class OrderTimeDeliveryTipController {

    private final OrderTimeDeliveryTipService service;
    private final OrderTimeDeliveryTipQueryService queryService;

    @ApiOperation(
        value = "시간별 배달팁 생성",
        tags = MgmtAdminSwaggerTags.ORDER_PRICE_DELIVERY_TIP
    )
    @PostMapping
    public ResponseEntity<Void> create(@PathVariable("storeId") long storeId,
                                       @Valid @RequestBody OrderTimeDeliveryTipRequest request,
                                       BindingResult result) {
        if(result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.create(storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(
        value = "시간별 배달팁 삭제",
        tags = MgmtAdminSwaggerTags.ORDER_PRICE_DELIVERY_TIP
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
        value = "시간별 배달팁 Store Id 조회",
        tags = MgmtAdminSwaggerTags.ORDER_PRICE_DELIVERY_TIP
    )
    @GetMapping
    public ResponseEntity<OrderTimeDeliveryTipResponse> getTipByStoreId(@PathVariable("storeId") long storeId) {
        final OrderTimeDeliveryTipResponse tipByStoreId = queryService.getTipByStoreId(storeId);
        return ResponseEntity.ok(tipByStoreId);
    }

    @ApiOperation(
        value = "시간별 배달팁 시간, 요일별로 조회",
        tags = MgmtAdminSwaggerTags.ORDER_PRICE_DELIVERY_TIP
    )
    @GetMapping(params = {"time", "dayOfWeek"})
    public ResponseEntity<OrderTimeDeliveryTipResponse> getTipByTime(
            @PathVariable("storeId") long storeId,
            OrderTimeDeliveryTipSearch search) {
        final OrderTimeDeliveryTipResponse tipByStoreId = queryService.getTipByTime(storeId,
                                                                                    search.getTime(),
                                                                                    search.getDayOfWeek());
        return ResponseEntity.ok(tipByStoreId);
    }

}
