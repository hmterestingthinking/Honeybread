package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipRequest;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("stores/{storeId}/order-time-delivery-tips")
public class OrderTimeDeliveryTipController {

    private final OrderTimeDeliveryTipService service;
    private final OrderTimeDeliveryTipQueryService queryService;

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

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<OrderTimeDeliveryTipResponse> getTipByStoreId(@PathVariable("storeId") long storeId) {
        final OrderTimeDeliveryTipResponse tipByStoreId = queryService.getTipByStoreId(storeId);
        return ResponseEntity.ok(tipByStoreId);
    }

    @GetMapping(params = "time")
    public ResponseEntity<OrderTimeDeliveryTipResponse> getTipByTime(
            @PathVariable("storeId") long storeId,
            @DateTimeFormat(pattern = "HH:mm") @RequestParam("time") LocalTime time) {
        final OrderTimeDeliveryTipResponse tipByStoreId = queryService.getTipByTime(storeId, time);
        return ResponseEntity.ok(tipByStoreId);
    }

}
