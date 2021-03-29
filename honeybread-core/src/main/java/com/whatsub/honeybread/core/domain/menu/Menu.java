package com.whatsub.honeybread.core.domain.menu;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.menu.listener.MenuListener;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import java.util.List;

@Getter
@Entity
@Table(name = "menus")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(MenuListener.class)
public class Menu extends BaseEntity {

    // 스토어
    @Column(nullable = false)
    private Long storeId;

    // 메뉴 명
    @Column(nullable = false)
    private String name;

    // 메뉴 이미지
    @Column(nullable = false)
    private String imageUrl;

    // 설명
    private String description;

    // 기본 판매 가격
    @Column(nullable = false)
    private Money price;

    // 대표 메뉴 여부
    @Column(nullable = false)
    private boolean isMain = Boolean.FALSE;

    // 인기 메뉴 여부
    @Column(nullable = false)
    private boolean isBest = Boolean.FALSE;

    // 메뉴 그룹
    @Column(nullable = false)
    private Long menuGroupId;

    // 음식 카테고리
    @Column(nullable = false)
    private Long categoryId;

    @BatchSize(size = 50)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuOptionGroup> optionGroups = List.of();

    @Builder
    public Menu(
        Long storeId,
        String name,
        String imageUrl,
        String description,
        Money price,
        boolean isMain,
        boolean isBest,
        Long menuGroupId,
        Long categoryId,
        List<MenuOptionGroup> optionGroups
    ) {
        this.storeId = storeId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.isMain = isMain;
        this.isBest = isBest;
        this.menuGroupId = menuGroupId;
        this.categoryId = categoryId;
        this.optionGroups = optionGroups;
    }

    public void update(Menu menu) {
        updateBasic(menu);
        updateOptions(menu);
    }

    private void updateBasic(Menu menu) {
        this.name = menu.getName();
        this.imageUrl = menu.getImageUrl();
        this.description = menu.getDescription();
        this.price = menu.getPrice();
        this.isMain = menu.isMain();
        this.isBest = menu.isBest();
        this.menuGroupId = menu.getMenuGroupId();
        this.categoryId = menu.getCategoryId();
    }

    private void updateOptions(Menu menu) {
        this.optionGroups.clear();
        this.optionGroups.addAll(menu.getOptionGroups());
    }
}
