package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipRequest;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RestController
@RequiredArgsConstructor
@RequestMapping("stores/{storeId}/order-price-delivery-tips")
public class OrderPriceDeliveryTipController {

    private final OrderPriceDeliveryTipQueryService queryService;
    private final OrderPriceDeliveryTipService service;

    @ApiOperation(
        value = "주문금액별 배달팁 목록 조회",
        tags = MgmtAdminSwaggerTags.ORDER_PRICE_DELIVERY_TIP
    )
    @GetMapping
    public ResponseEntity<List<OrderPriceDeliveryTipResponse>> getAllByStoreId(@PathVariable Long storeId) {
        final List<OrderPriceDeliveryTipResponse> deliveryTipResponses = queryService.getAllByStoreId(storeId);
        return ResponseEntity.ok(deliveryTipResponses);
    }

    @ApiOperation(
        value = "주문금액별 배달팁 생성",
        tags = MgmtAdminSwaggerTags.ORDER_PRICE_DELIVERY_TIP
    )
    @PostMapping
    public ResponseEntity<Void> create(@PathVariable Long storeId,
                                       @Valid @RequestBody OrderPriceDeliveryTipRequest request,
                                       BindingResult result) {
        if(result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.create(storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(
        value = "주문금액별 배달팁 삭제",
        tags = MgmtAdminSwaggerTags.ORDER_PRICE_DELIVERY_TIP
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
        value = "주문금액별 배달팁 검색",
        tags = MgmtAdminSwaggerTags.ORDER_PRICE_DELIVERY_TIP
    )

    @GetMapping(params = "price")
    public ResponseEntity<OrderPriceDeliveryTipResponse> getTipByOrderPrice(@PathVariable Long storeId,
                                                                            @RequestParam("price") int price) {
        final OrderPriceDeliveryTipResponse tipByOrderPrice
            = queryService.getTipByOrderPrice(storeId, Money.wons(price));
        return ResponseEntity.ok(tipByOrderPrice);
    }

}
