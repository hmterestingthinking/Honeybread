package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import com.whatsub.honeybread.core.domain.menu.repository.MenuRepository;
import com.whatsub.honeybread.core.domain.menu.validator.MenuValidator;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuOptionGroupRequest;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuOptionRequest;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = MenuService.class)
@RequiredArgsConstructor
class MenuServiceTest {
    final MenuService service;

    @MockBean
    MenuRepository repository;

    @MockBean
    MenuValidator validator;

    @Mock
    Menu menu;

    @Test
    void 벨리데이션_성공시_등록_성공() {
        // given
        메뉴_등록시_성공한다();
        벨리데이션_시도시_성공한다();

        // when
        메뉴_등록();

        // then
        벨리데이션이_수행되어야_한다();
        then(repository).should().save(any(Menu.class));
    }

    @Test
    void 벨리데이션_실패시_등록_실패() {
        // given
        메뉴_등록시_성공한다();
        벨리데이션_시도시_예외가_발생한다();

        // when
        assertThrows(ValidationException.class, this::메뉴_등록);

        // then
        벨리데이션이_수행되어야_한다();
        then(repository).should(never()).save(any(Menu.class));
    }

    @Test
    void 벨리데이션_성공시_수정_성공() {
        // given
        메뉴_조회시_성공한다();
        벨리데이션_시도시_성공한다();

        // when
        메뉴_수정();

        // then
        메뉴_조회가_수행되어야_한다();
        메뉴_수정이_수행되어야_한다();
        벨리데이션이_수행되어야_한다();
    }

    @Test
    void 벨리데이션_실패시_수정_실패() {
        // given
        메뉴_조회시_성공한다();
        벨리데이션_시도시_예외가_발생한다();

        // when
        assertThrows(ValidationException.class, this::메뉴_수정);

        // then
        메뉴_조회가_수행되어야_한다();
        메뉴_수정이_수행되어야_한다();
        벨리데이션이_수행되어야_한다();
    }

    @Test
    void 해당_메뉴가_없다면_수정_실패() {
        // given
        메뉴_조회시_실패한다();

        // when
        assertThrows(HoneyBreadException.class, this::메뉴_수정);

        // then
        메뉴_조회가_수행되어야_한다();
    }

    @Test
    void 해당메뉴가_있다면_삭제_성공() {
        // given
        메뉴_조회시_성공한다();

        // when
        메뉴_삭제();

        // then
        메뉴_조회가_수행되어야_한다();
        메뉴_삭제가_수행되어야_한다();
    }

    @Test
    void 해당메뉴가_없다면_삭제_실패() {
        // given
        메뉴_조회시_실패한다();

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, this::메뉴_삭제);

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MENU_NOT_FOUND);
    }

    /**
     * Given
     */
    private void 메뉴_등록시_성공한다() {
        given(menu.getId()).willReturn(1L);
        given(repository.save(any(Menu.class))).willReturn(menu);
    }

    private void 메뉴_조회시_성공한다() {
        given(repository.findById(anyLong())).willReturn(Optional.of(menu));
    }

    private void 메뉴_조회시_실패한다() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());
    }

    private void 벨리데이션_시도시_성공한다() {
        willDoNothing().given(validator).validate(any(Menu.class));
    }

    private void 벨리데이션_시도시_예외가_발생한다() {
        willThrow(ValidationException.class).given(validator).validate(any(Menu.class));
    }

    /**
     * When
     */
    private void 메뉴_등록() {
        service.create(기본판매가가_존재하는_찜닭메뉴_요청());
    }

    private void 메뉴_수정() {
        service.update(anyLong(), 기본판매가가_존재하는_찜닭메뉴_요청());
    }

    private void 메뉴_삭제() {
        service.delete(anyLong());
    }

    /**
     * Then
     */
    private void 벨리데이션이_수행되어야_한다() {
        then(validator).should().validate(any(Menu.class));
    }

    private void 메뉴_조회가_수행되어야_한다() {
        then(repository).should().findById(anyLong());
    }

    private void 메뉴_수정이_수행되어야_한다() {
        then(menu).should().update(any(Menu.class));
    }

    private void 메뉴_삭제가_수행되어야_한다() {
        then(repository).should().delete(any(Menu.class));
    }

    /**
     * Helper
     */
    private MenuRequest 기본판매가가_존재하는_찜닭메뉴_요청() {
        return MenuRequest.builder()
            .menuGroupId(1L)
            .categoryId(1L)
            .name("간장 찜닭")
            .description("존맛탱 간장 찜닭")
            .basicPrice(Money.wons(10000L))
            .isMain(Boolean.TRUE)
            .isBest(Boolean.TRUE)
            .optionGroups(
                List.of(
                    MenuOptionGroupRequest.builder()
                        .name("맛 선택")
                        .type(MenuOptionGroup.Type.BASIC)
                        .minimumSelectCount(1)
                        .maximumSelectCount(1)
                        .options(
                            List.of(
                                new MenuOptionRequest("순한맛", Money.ZERO),
                                new MenuOptionRequest("약간 매운맛", Money.ZERO),
                                new MenuOptionRequest("매운맛", Money.ZERO)
                            )
                        ).build()
                )
            ).build();
    }
}