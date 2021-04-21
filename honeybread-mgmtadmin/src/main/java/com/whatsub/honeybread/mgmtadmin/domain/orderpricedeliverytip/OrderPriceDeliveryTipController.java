package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("stores/{storeId}/order-price-delivery-tips")
public class OrderPriceDeliveryTipController {

    private final OrderPriceDeliveryTipQueryService queryService;

    @GetMapping
    public ResponseEntity<List<OrderPriceDeliveryTipResponse>> getAllByStoreId(@PathVariable final Long storeId) {
        final List<OrderPriceDeliveryTipResponse> deliveryTipResponses = queryService.getAllByStoreId(storeId);
        return ResponseEntity.ok(deliveryTipResponses);
    }

}
