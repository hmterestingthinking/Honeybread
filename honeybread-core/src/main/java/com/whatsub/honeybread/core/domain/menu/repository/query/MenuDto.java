package com.whatsub.honeybread.core.domain.menu.repository.query;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MenuDto {
    long id;
    String name;
    String imageUrl;
    Money price;
    boolean isMain;
    boolean isBest;
    long menuGroupId;
    long categoryId;
    List<MenuOptionGroupDto> optionGroups;

    public static MenuDto of(Menu menu, List<MenuOptionGroupDto> optionGroups) {
        return new MenuDto(
            menu.getId(),
            menu.getName(),
            menu.getImageUrl(),
            menu.getPrice(),
            menu.isMain(),
            menu.isBest(),
            menu.getMenuGroupId(),
            menu.getCategoryId(),
            optionGroups
        );
    }
}
