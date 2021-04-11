package com.whatsub.honeybread.core.domain.menu;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Entity
@Table(name = "menu_option_groups")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOptionGroup extends BaseEntity {
    public static final int MINIMUM_SELECT_DEFAULT = 1;

    // 옵션 타입 (기본옵션 (필수선택), 추가옵션)
    public enum Type {
        BASIC,
        OPTIONAL,
        ;
    }

    // 옵션 그룹 명
    @Column(nullable = false)
    private String name;

    // 옵션 그룹 타입
    @Column(nullable = false)
    private Type type;

    // 옵션 최소 선택 개수
    @Column(nullable = false)
    private int minimumSelectCount = MINIMUM_SELECT_DEFAULT;

    // 옵션 최대 선택 개수
    @Column(nullable = false)
    private int maximumSelectCount = MINIMUM_SELECT_DEFAULT;

    @BatchSize(size = 50)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_option_group_id")
    private List<MenuOption> options = List.of();

    @Builder
    public MenuOptionGroup(
        String name,
        Type type,
        int minimumSelectCount,
        int maximumSelectCount,
        List<MenuOption> options
    ) {
        this.name = name;
        this.type = type;
        this.minimumSelectCount = minimumSelectCount;
        this.maximumSelectCount = maximumSelectCount;
        this.options = options;
    }
}
