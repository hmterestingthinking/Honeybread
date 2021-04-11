package com.whatsub.honeybread.mgmtadmin.domain.menu.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("메뉴 옵션 요청")
@Data
public class MenuOptionRequest {

    @ApiModelProperty("옵션 명")
    @NotBlank(message = "옵션 명은 빈 값이 올 수 없습니다.")
    private String name;

    @ApiModelProperty("판매가")
    @NotNull(message = "판매가는 빈 값이 올 수 없습니다.")
    private Money price;

    public MenuOptionRequest(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
