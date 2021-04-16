package com.whatsub.honeybread.core.domain.menu.repository.query;

import com.whatsub.honeybread.core.domain.menu.MenuGroup;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
public class MenuGroupDto {
    long id;
    String name;
    String description;
    List<MenuDto> menus;

    public static MenuGroupDto of(MenuGroup menuGroup, List<MenuDto> menus) {
        return new MenuGroupDto(
            menuGroup.getId(),
            menuGroup.getName(),
            menuGroup.getDescription(),
            menus
        );
    }
}
