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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = MenuValidator.class)
@RequiredArgsConstructor
class MenuValidatorTest {

    final MenuValidator validator;

    @MockBean
    MenuGroupRepository groupRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @Mock
    Menu menu;

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
    @BeforeEach
    void setUp() {
        given(menu.getMenuGroupId()).willReturn(1L);
        given(menu.getCategoryId()).willReturn(1L);
        given(menu.getName()).willReturn("간장 찜닭");
        given(menu.getDescription()).willReturn("존맛탱 간장 찜닭");
        given(menu.getPrice()).willReturn(Money.wons(10_000));
        given(menu.isMain()).willReturn(Boolean.TRUE);
        given(menu.isBest()).willReturn(Boolean.FALSE);
        given(menu.getOptionGroups()).willReturn(
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
        );
    }

    @Test
    void 벨리데이션_조건이_모두_성립하면_성공() {
        // given
        카테고리_조회시_성공한다();
        메뉴그룹_조회시_성공한다();

        // when
        메뉴_벨리데이션();

        // then
        카테고리_조회가_수행되어야_한다();
        메뉴그룹_조회가_수행되어야_한다();
    }

    @Test
    void 카테고리가_없다면_예외_발생() {
        // given
        given(categoryRepository.existsById(anyLong())).willReturn(Boolean.FALSE);

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, this::메뉴_벨리데이션);

        // then
        카테고리_조회가_수행되어야_한다();
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 메뉴_그룹이_없다면_예외_발생() {
        // given
        카테고리_조회시_성공한다();
        given(groupRepository.existsById(anyLong())).willReturn(Boolean.FALSE);

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, this::메뉴_벨리데이션);

        // then
        카테고리_조회가_수행되어야_한다();
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MENU_GROUP_NOT_FOUND);
    }

    @Test
    void 기본판매가가_0원이고_기본옵션_그룹이_존재한다면_벨리데이션_성공() {
        // given
        카테고리_조회시_성공한다();
        메뉴그룹_조회시_성공한다();
        기본판매가_0원으로_지정();

        // when
        메뉴_벨리데이션();

        // then
        카테고리_조회가_수행되어야_한다();
        메뉴그룹_조회가_수행되어야_한다();
    }

    @Test
    void 기본판매가가_0원이고_기본옵션_그룹이_존재하지_않는다면_벨리데이션_실패() {
        // given
        카테고리_조회시_성공한다();
        메뉴그룹_조회시_성공한다();
        기본판매가_0원으로_지정();
        given(menu.getOptionGroups()).willReturn(
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
        );

        // when
        ValidationException ex = assertThrows(ValidationException.class, this::메뉴_벨리데이션);

        // then
        카테고리_조회가_수행되어야_한다();
        메뉴그룹_조회가_수행되어야_한다();
        assertThat(ex.getErrors().getErrorCount()).isEqualTo(1);
    }

    /**
     * Given
     */
    private void 카테고리_조회시_성공한다() {
        given(categoryRepository.existsById(anyLong())).willReturn(Boolean.TRUE);
    }

    private void 메뉴그룹_조회시_성공한다() {
        given(groupRepository.existsById(anyLong())).willReturn(Boolean.TRUE);
    }

    private void 기본판매가_0원으로_지정() {
        given(menu.getPrice()).willReturn(Money.ZERO);
    }

    /**
     * When
     */
    private void 메뉴_벨리데이션() {
        validator.validate(menu);
    }

    /**
     * Then
     */
    private void 카테고리_조회가_수행되어야_한다() {
        then(categoryRepository).should().existsById(anyLong());
    }

    private void 메뉴그룹_조회가_수행되어야_한다() {
        then(groupRepository).should().existsById(anyLong());
    }
}