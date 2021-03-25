package com.whatsub.honeybread.core.domain.menu;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "menu_options")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOption extends BaseEntity {
    public static final Money MINIMUM_PRICE = Money.ZERO;

    // 메뉴 옵션 명
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Money price = MINIMUM_PRICE;

    public static MenuOption create(String name, Money price) {
        MenuOption menuOption = new MenuOption();
        menuOption.name = name;
        menuOption.price = price;
        return menuOption;
    }
}
