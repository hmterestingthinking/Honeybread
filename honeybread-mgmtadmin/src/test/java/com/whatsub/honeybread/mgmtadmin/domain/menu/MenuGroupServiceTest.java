package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.whatsub.honeybread.core.domain.menu.MenuGroup;
import com.whatsub.honeybread.core.domain.menu.repository.MenuGroupRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuGroupRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = MenuGroupService.class)
@RequiredArgsConstructor
class MenuGroupServiceTest {
    final MenuGroupService service;

    @MockBean
    MenuGroupRepository repository;

    @Mock
    MenuGroup menuGroup;

    @Test
    void 메뉴그룹_생성_성공() {
        // given
        메뉴_그룹_등록시_성공한다();

        // when
        메뉴_그룹_생성();

        // then
        then(repository).should().save(any(MenuGroup.class));
    }

    @Test
    void 메뉴그룹_수정_성공() {
        // given
        메뉴_그룹_조회시_성공한다();

        // when
        메뉴_수정();

        // then
        메뉴_그룹_조회가_수행되어야_한다();
        then(menuGroup).should().update(any(MenuGroup.class));
    }

    @Test
    void 메뉴그룹이_없다면_수정_실패() {
        // given
        메뉴_그룹_조회시_실패한다();

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, this::메뉴_수정);

        // then
        메뉴_그룹_조회가_수행되어야_한다();
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MENU_GROUP_NOT_FOUND);
    }

    @Test
    void 메뉴그룹_삭제_성공() {
        // given
        메뉴_그룹_조회시_성공한다();

        // when
        메뉴_삭제();

        // then
        메뉴_그룹_조회가_수행되어야_한다();
        then(repository).should().delete(any(MenuGroup.class));
    }

    @Test
    void 메뉴그룹이_없다면_삭제_실패() {
        // given
        메뉴_그룹_조회시_실패한다();

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, this::메뉴_삭제);

        // then
        메뉴_그룹_조회가_수행되어야_한다();
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MENU_GROUP_NOT_FOUND);
    }

    /**
     * Given
     */
    private void 메뉴_그룹_등록시_성공한다() {
        given(menuGroup.getId()).willReturn(1L);
        given(repository.save(any(MenuGroup.class))).willReturn(menuGroup);
    }

    private void 메뉴_그룹_조회시_성공한다() {
        given(repository.findById(anyLong())).willReturn(Optional.of(menuGroup));
    }

    private void 메뉴_그룹_조회시_실패한다() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());
    }

    /**
     * When
     */
    private void 메뉴_그룹_생성() {
        service.create(메뉴_그룹_요청());
    }

    private void 메뉴_수정() {
        service.update(anyLong(), 메뉴_그룹_요청());
    }

    private void 메뉴_삭제() {
        service.delete(anyLong());
    }

    /**
     * Then
     */
    private void 메뉴_그룹_조회가_수행되어야_한다() {
        then(repository).should().findById(anyLong());
    }

    /**
     * Helper
     */
    private MenuGroupRequest 메뉴_그룹_요청() {
        return MenuGroupRequest.builder()
            .name("식사류")
            .description("식사 메뉴 입니다 ~")
            .storeId(1L)
            .build();
    }
}