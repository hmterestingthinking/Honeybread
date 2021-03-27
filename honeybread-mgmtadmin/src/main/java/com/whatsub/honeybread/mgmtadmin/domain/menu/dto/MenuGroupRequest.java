package com.whatsub.honeybread.mgmtadmin.domain.menu.dto;

import com.whatsub.honeybread.core.domain.menu.MenuGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("메뉴 그룹 요청")
@Data
@Builder
public class MenuGroupRequest {

    @ApiModelProperty("메뉴 그룹명")
    @NotBlank(message = "메뉴 그룹명은 빈 값이 올 수 없습니다.")
    private String name;

    @ApiModelProperty("메뉴 그룹설명")
    @NotBlank(message = "메뉴 그룹 설명은 빈 값이 올 수 없습니다.")
    private String description;

    @ApiModelProperty("스토어 아이디")
    @NotNull(message = "스토어 아이디는 빈 값이 올 수 없습니다.")
    @Min(value = 1, message = "잘못된 스토어 아이디 입니다.")
    private Long storeId;

    public MenuGroup toEntity() {
        return MenuGroup.builder()
            .name(this.name)
            .description(this.description)
            .storeId(this.storeId)
            .build();
    }
}
