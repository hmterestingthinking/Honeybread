package com.whatsub.honeybread.core.domain.menu;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "menu_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuGroup extends BaseEntity {

    // 메뉴 그룹명
    @Column(nullable = false)
    private String name;

    // 그룹 설명
    private String description;

    // 스토어
    @Column(nullable = false)
    private Long storeId;
}
