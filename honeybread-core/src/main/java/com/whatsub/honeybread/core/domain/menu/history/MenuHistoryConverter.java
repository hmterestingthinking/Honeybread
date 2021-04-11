package com.whatsub.honeybread.core.domain.menu.history;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.MenuOption;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuHistoryConverter {

    public MenuHistory convert(Menu menu) {
        return MenuHistory.builder()
            .menuId(menu.getId())
            .storeId(menu.getStoreId())
            .name(menu.getName())
            .imageUrl(menu.getImageUrl())
            .description(menu.getDescription())
            .price(menu.getPrice().getValue())
            .isMain(menu.isMain())
            .isBest(menu.isBest())
            .menuGroupId(menu.getMenuGroupId())
            .categoryId(menu.getCategoryId())
            .optionGroups(
                convertGroups(menu.getOptionGroups())
            )
            .build();
    }

    private List<MenuHistory.OptionGroup> convertGroups(List<MenuOptionGroup> optionGroups) {
        return optionGroups.stream()
            .map(optionGroup -> MenuHistory.OptionGroup.builder()
                .optionGroupId(optionGroup.getId())
                .name(optionGroup.getName())
                .type(
                    convertType(optionGroup.getType())
                )
                .maximumSelectCount(optionGroup.getMaximumSelectCount())
                .minimumSelectCount(optionGroup.getMinimumSelectCount())
                .options(
                    convertOptions(optionGroup.getOptions())
                )
                .build())
            .collect(Collectors.toList());
    }

    private List<MenuHistory.Option> convertOptions(List<MenuOption> options) {
        return options.stream()
            .map(option -> MenuHistory.Option.builder()
                .optionId(option.getId())
                .name(option.getName())
                .price(option.getPrice().getValue())
                .build())
            .collect(Collectors.toList());
    }

    private MenuHistory.OptionGroup.Type convertType(MenuOptionGroup.Type type) {
        if (type == MenuOptionGroup.Type.BASIC) {
            return MenuHistory.OptionGroup.Type.BASIC;
        }
        return MenuHistory.OptionGroup.Type.OPTIONAL;
    }
}
