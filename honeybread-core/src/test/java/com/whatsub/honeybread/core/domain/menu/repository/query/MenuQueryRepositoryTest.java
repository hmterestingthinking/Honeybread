package com.whatsub.honeybread.core.domain.menu.repository.query;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.MenuGroup;
import com.whatsub.honeybread.core.domain.menu.MenuOption;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import com.whatsub.honeybread.core.domain.menu.repository.MenuGroupRepository;
import com.whatsub.honeybread.core.domain.menu.repository.MenuRepository;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@RequiredArgsConstructor
class MenuQueryRepositoryTest {
    final MenuGroupRepository groupRepository;
    final MenuRepository repository;
    final MenuQueryRepository queryRepository;

    static final Long STORE_ID = 1L;

    @Test
    void 메뉴_그룹_하위에_메뉴가_존재하지_않는다면_조회대상에서_제외된다() throws Exception {
        // given
        MenuGroup menuGroup1 = 메뉴_그룹_생성("찜닭");
        MenuGroup menuGroup2 = 메뉴_그룹_생성("사이드 메뉴");
        MenuGroup menuGroup3 = 메뉴_그룹_생성("주류");

        groupRepository.saveAll(List.of(menuGroup1, menuGroup2, menuGroup3));

        Menu menu1 = 메뉴_생성(
            menuGroup1.getId(),
            "간장 찜닭",
            "존맛탱 간장 찜닭",
            10000L,
            "맵기 선택",
            "순한맛",
            "보통맛",
            "매운맛"
        );

        Menu menu2 = 메뉴_생성(
            menuGroup1.getId(),
            "갈비 찜닭",
            "존맛탱 갈비 찜닭",
            15000L,
            "맵기 선택",
            "순한맛",
            "보통맛",
            "매운맛"
        );

        Menu menu3 = 메뉴_생성(
            menuGroup1.getId(),
            "도련님 찜닭",
            "존맛탱 도련님 찜닭",
            15000L,
            "맵기 선택",
            "순한맛",
            "보통맛",
            "매운맛"
        );

        Menu sideMenu = 메뉴_생성(
            menuGroup2.getId(),
            "주먹밥",
            "맛있는 주먹밥",
            5000L,
            "토핑 선택",
            "참치"
        );

        repository.saveAll(List.of(menu1, menu2, menu3, sideMenu));

        // when
        List<MenuGroupDto> menus = queryRepository.findAllByStoreId(STORE_ID);

        // then
        assertThat(menus.size()).isEqualTo(2);
    }

    private Menu 메뉴_생성(
        Long menuGroupId,
        String menuName,
        String menuDesc,
        long basicPrice,
        String menuOptionGroupName,
        String... optionName
    ) {
        return Menu.builder()
            .storeId(STORE_ID)
            .menuGroupId(menuGroupId)
            .categoryId(1L)
            .name(menuName)
            .description(menuDesc)
            .imageUrl("https://www.naver.com")
            .price(Money.wons(basicPrice))
            .isMain(Boolean.TRUE)
            .isBest(Boolean.TRUE)
            .optionGroups(
                List.of(
                    MenuOptionGroup.builder()
                        .name(menuOptionGroupName)
                        .type(MenuOptionGroup.Type.BASIC)
                        .minimumSelectCount(1)
                        .maximumSelectCount(1)
                        .options(
                            Arrays.stream(optionName)
                                .map(v -> MenuOption.create(v, Money.ZERO))
                                .collect(Collectors.toList())
                        ).build()
                )
            ).build();
    }

    private MenuGroup 메뉴_그룹_생성(String name) {
        return MenuGroup.builder()
            .storeId(STORE_ID)
            .name(name)
            .description(name)
            .build();
    }

}