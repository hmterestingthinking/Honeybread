package com.whatsub.honeybread.core.domain.menu.repository.query;

import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import lombok.Value;

import java.util.List;

@Value
public class MenuOptionGroupDto {
    long id;
    String name;
    MenuOptionGroup.Type type;
    int minimumSelectCount;
    int maximumSelectCount;
    List<MenuOptionDto> options;

    public static MenuOptionGroupDto of(MenuOptionGroup optionGroup, List<MenuOptionDto> options) {
        return new MenuOptionGroupDto(
            optionGroup.getId(),
            optionGroup.getName(),
            optionGroup.getType(),
            optionGroup.getMinimumSelectCount(),
            optionGroup.getMaximumSelectCount(),
            options
        );
    }
}
