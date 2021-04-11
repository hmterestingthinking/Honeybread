package com.whatsub.honeybread.core.domain.menu.repository.query;

import com.whatsub.honeybread.core.domain.menu.MenuOption;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.Value;

@Value
public class MenuOptionDto {
    long id;
    String name;
    Money price;

    public static MenuOptionDto of(MenuOption option) {
        return new MenuOptionDto(option.getId(), option.getName(), option.getPrice());
    }
}
