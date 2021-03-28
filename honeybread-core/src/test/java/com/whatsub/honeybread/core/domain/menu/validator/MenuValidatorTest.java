package com.whatsub.honeybread.core.domain.menu.validator;

import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.MenuOption;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import com.whatsub.honeybread.core.domain.menu.repository.MenuGroupRepository;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = MenuValidator.class)
@RequiredArgsConstructor
class MenuValidatorTest {

    final MenuValidator validator;

    @MockBean
    MenuGroupRepository groupRepository;

    @MockBean
    CategoryRepository categoryRepository;


    /**
     * 해당하는 카테고리가 있는지 검증
     * 해당하는 메뉴 그룹이 있는지 검증
     * <p>
     * 옵션 그룹의 최소 선택 개수는 1개 이상이어야 한다.
     * 옵션 그룹의 최대 선택 개수는 최소 선택개수 보다 작을 수 없다.
     * <p>
     * 옵션 그룹 타입이 기본형 일경우
     * 0개 이상 존재할 수 있다.
     * 기본 판매가가 0원이라면, 1개가 반드시 존재해야 한다.
     * <p>
     * 옵션 그룹 타입이 옵셔널 일경우
     * 0개 이상 존재할 수 있다.
     * 판매가가 0원으로 지정될 수 있다.
     * 기본 판매가에 + 추가 계산되는 가격이다.
     */

    @Test
    void 벨리데이션_조건이_모두_성립하면_성공() {
        // given
        final Menu menu = 기본판매가가_존재하는_찜닭메뉴();

        given(categoryRepository.existsById(anyLong())).willReturn(Boolean.TRUE);
        given(groupRepository.existsById(anyLong())).willReturn(Boolean.TRUE);

        // when
        validator.validate(menu);

        // then
        verify(categoryRepository).existsById(anyLong());
        verify(groupRepository).existsById(anyLong());
    }

    @Test
    void 카테고리가_없다면_예외_발생() {
        // given
        final Menu menu = 기본판매가가_존재하는_찜닭메뉴();

        given(categoryRepository.existsById(anyLong())).willReturn(Boolean.FALSE);

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, () -> validator.validate(menu));

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 메뉴_그룹이_없다면_예외_발생() {
        // given
        final Menu menu = 기본판매가가_존재하는_찜닭메뉴();

        given(categoryRepository.existsById(anyLong())).willReturn(Boolean.TRUE);
        given(groupRepository.existsById(anyLong())).willReturn(Boolean.FALSE);

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, () -> validator.validate(menu));

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MENU_GROUP_NOT_FOUND);
    }

    @Test
    void 기본판매가가_0원이고_기본옵션_그룹이_존재한다면_벨리데이션_성공() {
        // given
        final Menu menu = Menu.builder()
            .menuGroupId(1L)
            .categoryId(1L)
            .name("간장 찜닭")
            .description("존맛탱 간장 찜닭")
            .price(Money.ZERO)
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
                                MenuOption.create("순한맛", Money.wons(10000L)),
                                MenuOption.create("약간 매운맛", Money.wons(10000L)),
                                MenuOption.create("매운맛", Money.wons(10000L))
                            )
                        ).build()
                )
            ).build();

        given(categoryRepository.existsById(anyLong())).willReturn(Boolean.TRUE);
        given(groupRepository.existsById(anyLong())).willReturn(Boolean.TRUE);

        // when
        validator.validate(menu);

        // then
        verify(categoryRepository).existsById(anyLong());
        verify(groupRepository).existsById(anyLong());
    }

    @Test
    void 기본판매가가_0원이고_기본옵션_그룹이_존재하지_않는다면_벨리데이션_실패() {
        // given
        final Menu menu = Menu.builder()
            .menuGroupId(1L)
            .categoryId(1L)
            .name("간장 찜닭")
            .description("존맛탱 간장 찜닭")
            .price(Money.ZERO)
            .isMain(Boolean.TRUE)
            .isBest(Boolean.TRUE)
            .optionGroups(
                List.of(
                    MenuOptionGroup.builder()
                        .name("맛 선택")
                        .type(MenuOptionGroup.Type.OPTIONAL)
                        .minimumSelectCount(1)
                        .maximumSelectCount(1)
                        .options(
                            List.of(
                                MenuOption.create("순한맛", Money.ZERO),
                                MenuOption.create("약간 매운맛", Money.ZERO),
                                MenuOption.create("매운맛", Money.ZERO)
                            )
                        ).build()
                )
            ).build();

        given(categoryRepository.existsById(anyLong())).willReturn(Boolean.TRUE);
        given(groupRepository.existsById(anyLong())).willReturn(Boolean.TRUE);

        // when
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(menu));

        // then
        assertThat(ex.getErrors().getErrorCount()).isEqualTo(1);
    }

    private Menu 기본판매가가_존재하는_찜닭메뉴() {
        return Menu.builder()
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
                        ).build()
                )
            ).build();
    }
}