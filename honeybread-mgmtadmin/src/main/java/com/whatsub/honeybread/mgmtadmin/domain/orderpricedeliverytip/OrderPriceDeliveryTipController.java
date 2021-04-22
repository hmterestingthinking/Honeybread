package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipRequest;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipResponse;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("stores/{storeId}/order-price-delivery-tips")
public class OrderPriceDeliveryTipController {

    private final OrderPriceDeliveryTipQueryService queryService;
    private final OrderPriceDeliveryTipService service;

    @GetMapping
    public ResponseEntity<List<OrderPriceDeliveryTipResponse>> getAllByStoreId(@PathVariable Long storeId) {
        final List<OrderPriceDeliveryTipResponse> deliveryTipResponses = queryService.getAllByStoreId(storeId);
        return ResponseEntity.ok(deliveryTipResponses);
    }

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

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
