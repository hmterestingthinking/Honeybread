package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.whatsub.honeybread.core.domain.menu.MenuGroup;
import com.whatsub.honeybread.core.domain.menu.repository.MenuGroupRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuGroupRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = MenuGroupService.class)
@RequiredArgsConstructor
class MenuGroupServiceTest {
    final MenuGroupService service;

    @MockBean
    MenuGroupRepository repository;

    @Test
    void 메뉴그룹_생성_성공() throws Exception {
        // given
        final MenuGroupRequest request = 식사류_메뉴_그룹_요청();
        final MenuGroup mockGroup = mock(MenuGroup.class);
        given(mockGroup.getId()).willReturn(1L);


        given(repository.save(any(MenuGroup.class))).willReturn(mockGroup);

        // when
        service.create(request);

        // then
        verify(repository).save(any(MenuGroup.class));
    }

    @Test
    void 메뉴그룹_수정_성공() throws Exception {
        // given
        final long menuGroupId = 1L;
        final MenuGroupRequest request = 식사류_메뉴_그룹_요청();
        final MenuGroup mockGroup = mock(MenuGroup.class);

        given(repository.findById(anyLong())).willReturn(Optional.of(mockGroup));

        // when
        service.update(menuGroupId, request);

        // then
        verify(repository).findById(anyLong());
        verify(mockGroup).update(any(MenuGroup.class));
    }

    @Test
    void 메뉴그룹이_없다면_수정_실패() throws Exception {
        // given
        final long menuGroupId = 1L;
        final MenuGroupRequest request = 식사류_메뉴_그룹_요청();
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, () -> service.update(menuGroupId, request));

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MENU_GROUP_NOT_FOUND);
    }

    @Test
    void 메뉴그룹_삭제_성공() throws Exception {
        // given
        final long menuGroupId = 1L;

        given(repository.findById(anyLong())).willReturn(Optional.of(mock(MenuGroup.class)));

        // when
        service.delete(menuGroupId);

        // then
        verify(repository).findById(anyLong());
        verify(repository).delete(any(MenuGroup.class));
    }

    @Test
    void 메뉴그룹이_없다면_삭제_실패() throws Exception {
        // given
        final long menuGroupId = 1L;

        given(repository.findById(anyLong())).willReturn(Optional.empty());

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, () -> service.delete(menuGroupId));

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MENU_GROUP_NOT_FOUND);
    }

    private MenuGroupRequest 식사류_메뉴_그룹_요청() {
        return MenuGroupRequest.builder()
            .name("식사류")
            .description("식사 메뉴 입니다 ~")
            .storeId(1L)
            .build();
    }
}