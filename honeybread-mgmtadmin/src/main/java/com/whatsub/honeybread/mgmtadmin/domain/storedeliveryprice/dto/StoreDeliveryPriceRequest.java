package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("주소별 등록 요청")
@Value
public class StoreDeliveryPriceRequest {

    @NotNull(message = "Store Id는 NULL일 수 없습니다.")
    @ApiModelProperty(value = "스토어ID", example = "1")
    Long storeId;

    @NotBlank(message = "주소는 빈 값이 올 수 없습니다.")
    @ApiModelProperty(value = "검색가능주소", example = "서울시 강남구 수서동")
    String searchableDeliveryAddress;

    @NotNull(message = "가격은 NULL일 수 없습니다.")
    @ApiModelProperty(value = "배달비", example = "1000")
    Money price;

    public StoreDeliveryPrice toEntity() {
        return StoreDeliveryPrice.builder()
            .price(this.price)
            .searchableDeliveryAddress(this.searchableDeliveryAddress)
            .storeId(this.storeId)
            .build();
    }
}
