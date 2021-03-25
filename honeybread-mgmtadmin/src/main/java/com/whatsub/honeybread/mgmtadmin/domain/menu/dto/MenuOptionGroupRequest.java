package com.whatsub.honeybread.mgmtadmin.domain.menu.dto;

import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@ApiModel("메뉴 옵션 그룹 요청")
@Data
@Builder
public class MenuOptionGroupRequest {

    @ApiModelProperty("옵션 그룹 아이디")
    private Long id;

    @ApiModelProperty("옵션 그룹 명")
    private String name;

    @ApiModelProperty("옵션 그룹 타입")
    private MenuOptionGroup.Type type;

    @ApiModelProperty("최소 선택 개수")
    private int minimumSelectCount;

    @ApiModelProperty("최대 선택 개수")
    private int maximumSelectCount;

    @ApiModelProperty("옵션 목록")
    private List<MenuOptionRequest> options;
}
