package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

import javax.validation.constraints.NotNull;

@ApiModel("주소별 배달금액 수정 요청")
@Value
public class StoreDeliveryPriceModifyRequest {

    @NotNull(message = "가격은 NULL일 수 없습니다.")
    @ApiModelProperty(value = "배달비", example = "1000")
    Money price;

    public StoreDeliveryPrice toEntity() {
        return StoreDeliveryPrice.builder()
            .price(this.price)
            .build();
    }
}
