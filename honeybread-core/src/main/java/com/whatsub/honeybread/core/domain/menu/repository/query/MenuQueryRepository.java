package com.whatsub.honeybread.core.domain.menu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.MenuGroup;
import com.whatsub.honeybread.core.domain.menu.MenuOption;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.whatsub.honeybread.core.domain.menu.QMenu.menu;
import static com.whatsub.honeybread.core.domain.menu.QMenuGroup.menuGroup;
import static java.util.stream.Collectors.*;

@Repository
@Transactional(readOnly = true)
public class MenuQueryRepository {

    private final JPAQueryFactory queryFactory;

    public MenuQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<MenuGroupDto> findAllByStoreId(final Long storeId) {
        List<Menu> menus = queryFactory
            .selectFrom(menu)
            .where(menu.storeId.eq(storeId))
            .fetch();

        Set<Long> menuGroupIds = getMenuGroupIds(menus);

        List<MenuGroup> menuGroups = queryFactory
            .selectFrom(menuGroup)
            .where(menuGroup.id.in(menuGroupIds))
            .fetch();

        Map<Long, List<MenuDto>> groupByMenuGroupId = menus.stream()
            .map(menu -> MenuDto.of(menu, getOptionGroups(menu.getOptionGroups())))
            .collect(
                groupingBy(
                    MenuDto::getMenuGroupId,
                    mapping(o -> o, toList())
                )
            );

        return menuGroups.stream()
            .map(menuGroup ->
                MenuGroupDto.of(
                    menuGroup,
                    groupByMenuGroupId.get(menuGroup.getId())
                )
            ).collect(toList());
    }

    private Set<Long> getMenuGroupIds(final List<Menu> menus) {
        return menus.stream()
            .map(Menu::getMenuGroupId)
            .collect(Collectors.toSet());
    }

    private List<MenuOptionGroupDto> getOptionGroups(final List<MenuOptionGroup> menuOptionGroups) {
        return menuOptionGroups.stream()
            .map(optionGroup ->
                MenuOptionGroupDto.of(
                    optionGroup,
                    getOptions(optionGroup.getOptions())
                )
            ).collect(Collectors.toList());
    }

    private List<MenuOptionDto> getOptions(final List<MenuOption> menuOptions) {
        return menuOptions.stream()
            .map(MenuOptionDto::of)
            .collect(Collectors.toList());
    }
}
