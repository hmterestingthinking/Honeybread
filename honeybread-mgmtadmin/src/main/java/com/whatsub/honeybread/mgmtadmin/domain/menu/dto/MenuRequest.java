package com.whatsub.honeybread.mgmtadmin.domain.menu.dto;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.MenuOption;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import com.whatsub.honeybread.core.domain.model.Money;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel("메뉴 요청")
@Data
@Builder
public class MenuRequest {
    @ApiModelProperty("스토어 아이디")
    @NotNull(message = "스토어 아이디는 빈 값이 올 수 없습니다.")
    @Min(value = 1, message = "잘못된 스토어 아이디 입니다.")
    private Long storeId;

    @ApiModelProperty("메뉴 그룹 아이디")
    @NotNull(message = "메뉴 그룹 아이디는 빈 값이 올 수 없습니다.")
    @Min(value = 1, message = "잘못된 메뉴 그룹 아이디 입니다.")
    private Long menuGroupId;

    @ApiModelProperty("메뉴 카테고리 아이디")
    @NotNull(message = "메뉴 카테고리 아이디는 빈 값이 올 수 없습니다.")
    @Min(value = 1, message = "잘못된 메뉴 카테고리 아이디 입니다.")
    private Long categoryId;

    @ApiModelProperty("메뉴 명")
    @NotBlank(message = "메뉴 명 빈 값이 올 수 없습니다.")
    private String name;

    @ApiModelProperty("메뉴 설명")
    @NotBlank(message = "메뉴 명 빈 값이 올 수 없습니다.")
    private String description;

    @ApiModelProperty("기본 판매 가격")
    @NotNull(message = "기본 판매 가격은 빈 값이 올 수 없습니다.")
    private Money basicPrice;

    @ApiModelProperty("대표 메뉴 여부")
    private boolean isMain;

    @ApiModelProperty("인기 메뉴 여부")
    private boolean isBest;

    @ApiModelProperty("메뉴 옵션 그룹 목록")
    @NotEmpty(message = "메뉴 옵션 그룹 목록은 빈 값이 올 수 없습니다.")
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
                getOptionGroupEntities()
            )
            .build();
    }

    private List<MenuOptionGroup> getOptionGroupEntities() {
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
