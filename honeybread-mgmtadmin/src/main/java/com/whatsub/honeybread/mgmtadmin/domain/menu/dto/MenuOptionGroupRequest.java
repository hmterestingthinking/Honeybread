package com.whatsub.honeybread.mgmtadmin.domain.menu.dto;

import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("메뉴 옵션 그룹 요청")
@Data
@Builder
public class MenuOptionGroupRequest {

    @ApiModelProperty("옵션 그룹 명")
    @NotBlank(message = "옵션 그룹 명은 빈 값이 올 수 없습니다.")
    private String name;

    @ApiModelProperty("옵션 그룹 타입")
    @NotNull(message = "옵션 그룹 타입은 빈 값이 올 수 없습니다.")
    private MenuOptionGroup.Type type;

    @ApiModelProperty("최소 선택 개수")
    private int minimumSelectCount;

    @ApiModelProperty("최대 선택 개수")
    private int maximumSelectCount;

    @ApiModelProperty("옵션 목록")
    @NotEmpty(message = "옵션 목록은 빈 값이 올 수 없습니다.")
    private List<MenuOptionRequest> options;
}
