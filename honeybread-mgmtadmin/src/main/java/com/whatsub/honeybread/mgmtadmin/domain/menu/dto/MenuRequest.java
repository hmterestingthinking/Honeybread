package com.whatsub.honeybread.mgmtadmin.domain.menu.dto;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.MenuOption;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import com.whatsub.honeybread.core.domain.model.Money;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel("메뉴 요청")
@Data
@Builder
public class MenuRequest {
    @ApiModelProperty("스토어 아이디")
    private Long storeId;

    @ApiModelProperty("메뉴 그룹 아이디")
    private Long menuGroupId;

    @ApiModelProperty("메뉴 카테고리 아이디")
    private Long categoryId;

    @ApiModelProperty("메뉴 명")
    private String name;

    @ApiModelProperty("메뉴 설명")
    private String description;

    @ApiModelProperty("기본 판매 가격")
    private Money basicPrice;

    @ApiModelProperty("대표 메뉴 여부")
    private boolean isMain;

    @ApiModelProperty("인기 메뉴 여부")
    private boolean isBest;

    @ApiModelProperty("메뉴 옵션 그룹 목록")
    private List<MenuOptionGroupRequest> optionGroups;

    public Menu toEntity() {
        return Menu.builder()
            .storeId(this.storeId)
            .menuGroupId(this.menuGroupId)
            .categoryId(this.categoryId)
            .name(this.name)
            .description(this.description)
            .price(this.basicPrice)
            .isMain(this.isMain)
            .isBest(this.isBest)
            .optionGroups(
                getOptionGroups()
            )
            .build();
    }

    private List<MenuOptionGroup> getOptionGroups() {
        return this.optionGroups.stream()
            .map(optionGroup -> MenuOptionGroup.builder()
                .name(optionGroup.getName())
                .type(optionGroup.getType())
                .minimumSelectCount(optionGroup.getMinimumSelectCount())
                .maximumSelectCount(optionGroup.getMaximumSelectCount())
                .options(
                    optionGroup.getOptions().stream()
                        .map(option -> MenuOption.create(option.getName(), option.getPrice()))
                        .collect(Collectors.toList())
                )
                .build())
            .collect(Collectors.toList());
    }
}
