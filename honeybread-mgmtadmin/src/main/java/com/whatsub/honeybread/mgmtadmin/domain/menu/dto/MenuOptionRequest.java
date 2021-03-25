package com.whatsub.honeybread.mgmtadmin.domain.menu.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("메뉴 옵션 요청")
@Data
public class MenuOptionRequest {
    @ApiModelProperty("메뉴 옵션 아이디")
    private Long id;

    @ApiModelProperty("옵션 명")
    private String name;

    @ApiModelProperty("판매가")
    private Money price;

    public MenuOptionRequest(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
