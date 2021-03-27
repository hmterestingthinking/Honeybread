package com.whatsub.honeybread.core.domain.menu.history;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.MenuOption;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = MenuHistoryConverter.class)
@RequiredArgsConstructor
class MenuHistoryConverterTest {

    final MenuHistoryConverter converter;

    @Test
    void 메뉴_엔티티_변환에_성공한다() {
        // given
        Menu entity = Menu.builder()
            .menuGroupId(1L)
            .categoryId(1L)
            .name("간장 찜닭")
            .description("존맛탱 간장 찜닭")
            .price(Money.wons(10000L))
            .isMain(Boolean.TRUE)
            .isBest(Boolean.TRUE)
            .optionGroups(
                List.of(
                    MenuOptionGroup.builder()
                        .name("맛 선택")
                        .type(MenuOptionGroup.Type.BASIC)
                        .minimumSelectCount(1)
                        .maximumSelectCount(1)
                        .options(
                            List.of(
                                MenuOption.create("순한맛", Money.ZERO),
                                MenuOption.create("약간 매운맛", Money.ZERO),
                                MenuOption.create("매운맛", Money.ZERO)
                            )
                        )
                        .build()
                )
            )
            .build();

        // when
        MenuHistory history = converter.convert(entity);

        // then
        assertThat(history.getId()).isEqualTo(entity.getId());
        assertThat(history.getMenuGroupId()).isEqualTo(entity.getMenuGroupId());
        assertThat(history.getCategoryId()).isEqualTo(entity.getCategoryId());
        assertThat(history.getName()).isEqualTo(entity.getName());
        assertThat(history.getPrice()).isEqualTo(entity.getPrice().getValue());
        assertThat(history.getPrice()).isEqualTo(entity.getPrice().getValue());
        assertThat(history.isMain()).isEqualTo(entity.isMain());
        assertThat(history.isBest()).isEqualTo(entity.isBest());
        assertThat(history.getOptionGroups().size()).isEqualTo(entity.getOptionGroups().size());

        for (int i = 0; i < history.getOptionGroups().size(); i++) {
            MenuHistory.OptionGroup optionGroup = history.getOptionGroups().get(i);
            MenuOptionGroup menuOptionGroup = entity.getOptionGroups().get(i);

            assertThat(optionGroup.getOptionGroupId()).isEqualTo(menuOptionGroup.getId());
            assertThat(optionGroup.getName()).isEqualTo(menuOptionGroup.getName());
            assertThat(optionGroup.getMinimumSelectCount()).isEqualTo(menuOptionGroup.getMinimumSelectCount());
            assertThat(optionGroup.getMaximumSelectCount()).isEqualTo(menuOptionGroup.getMaximumSelectCount());

            assertThat(optionGroup.getOptions().size()).isEqualTo(menuOptionGroup.getOptions().size());

            for (int j = 0; j < optionGroup.getOptions().size(); j++) {
                MenuHistory.Option option = optionGroup.getOptions().get(j);
                MenuOption menuOption = menuOptionGroup.getOptions().get(j);

                assertThat(option.getOptionId()).isEqualTo(menuOption.getId());
                assertThat(option.getName()).isEqualTo(menuOption.getName());
                assertThat(option.getPrice()).isEqualTo(menuOption.getPrice().getValue());
            }
        }

    }
}