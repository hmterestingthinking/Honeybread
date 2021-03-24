package com.whatsub.honeybread.core.domain.menu;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Table(name = "menus")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    // 기본 판매 가격 Converter 정의 필요
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuOptionGroup> optionGroups = List.of();
}
