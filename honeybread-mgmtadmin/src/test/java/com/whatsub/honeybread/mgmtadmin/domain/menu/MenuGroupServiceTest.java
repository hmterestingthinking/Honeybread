package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.whatsub.honeybread.core.domain.menu.MenuGroup;
import com.whatsub.honeybread.core.domain.menu.repository.MenuGroupRepository;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuGroupRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import static org.mockito.ArgumentMatchers.any;
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
        final MenuGroupRequest request = 식사류_메뉴_그륩_요청();
        final MenuGroup mockGroup = mock(MenuGroup.class);
        given(mockGroup.getId()).willReturn(1L);


        given(repository.save(any(MenuGroup.class))).willReturn(mockGroup);

        // when
        service.create(request);

        // then
        verify(repository).save(any(MenuGroup.class));
    }

    private MenuGroupRequest 식사류_메뉴_그륩_요청() {
        return MenuGroupRequest.builder()
            .name("식사류")
            .description("식사 메뉴 입니다 ~")
            .storeId(1L)
            .build();
    }
}