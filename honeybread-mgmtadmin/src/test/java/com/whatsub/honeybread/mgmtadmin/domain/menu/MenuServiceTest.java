package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import com.whatsub.honeybread.core.domain.menu.repository.MenuRepository;
import com.whatsub.honeybread.core.domain.menu.validator.MenuValidator;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuOptionGroupRequest;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuOptionRequest;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;

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

    @Test
    void 벨리데이션_성공시_메뉴_등록_성공() {
        // given
        final MenuRequest request = 기본판매가가_존재하는_찜닭메뉴_생성_요청();

        Menu mockMenu = mock(Menu.class);
        given(mockMenu.getId()).willReturn(1L);

        willDoNothing().given(validator).validate(any(Menu.class));
        given(repository.save(any(Menu.class))).willReturn(mockMenu);

        // when
        service.create(request);

        // then
        verify(validator).validate(any(Menu.class));
        verify(repository).save(any(Menu.class));
    }

    @Test
    void 벨리데이션_실패시_메뉴_등록_실패() {
        // given
        final MenuRequest request = 기본판매가가_존재하는_찜닭메뉴_생성_요청();

        Menu mockMenu = mock(Menu.class);
        given(mockMenu.getId()).willReturn(1L);

        willThrow(ValidationException.class).given(validator).validate(any(Menu.class));
        given(repository.save(any(Menu.class))).willReturn(mockMenu);

        // when
        assertThrows(ValidationException.class, () -> service.create(request));

        // then
        verify(validator).validate(any(Menu.class));
        verify(repository, never()).save(any(Menu.class));
    }

    private MenuRequest 기본판매가가_존재하는_찜닭메뉴_생성_요청() {
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