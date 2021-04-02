package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import com.whatsub.honeybread.core.domain.menu.repository.query.MenuDto;
import com.whatsub.honeybread.core.domain.menu.repository.query.MenuGroupDto;
import com.whatsub.honeybread.core.domain.menu.repository.query.MenuQueryRepository;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuGroupRequest;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuOptionGroupRequest;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuOptionRequest;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(MenuController.class)
@RequiredArgsConstructor
class MenuControllerTest {

    static final String BASE_URL = "/menus";
    static final String GROUP_BASE_URL = BASE_URL + "/groups";

    final MockMvc mockMvc;

    final ObjectMapper mapper;

    @MockBean
    MenuService service;

    @MockBean
    MenuGroupService groupService;

    @MockBean
    MenuQueryRepository queryRepository;

    @Test
    void 스토어의_메뉴목록_조회에_성공한다() throws Exception {
        // given
        List<MenuGroupDto> menus = List.of(
            MenuGroupDto.builder()
                .id(1L)
                .name("식사류")
                .description("식사류 입니다")
                .menus(
                    List.of(
                        MenuDto.builder()
                            .id(1L)
                            .name("볶음밥")
                            .menuGroupId(1L)
                            .categoryId(1L)
                            .imageUrl("https://www.naver.com")
                            .isMain(true)
                            .isBest(true)
                            .price(Money.wons(10000))
                            .build()
                    )
                )
                .build()
        );
        
        given(queryRepository.findAllByStoreId(anyLong())).willReturn(menus);

        // when
        ResultActions result = mockMvc.perform(
            get(BASE_URL + "/stores/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id").exists())
            .andExpect(jsonPath("$.[0].name").exists())
            .andExpect(jsonPath("$.[0].description").exists())
            .andExpect(jsonPath("$.[0].menus").isNotEmpty())
            .andExpect(jsonPath("$.[0].menus[0].id").exists())
            .andExpect(jsonPath("$.[0].menus[0].name").exists())
            .andExpect(jsonPath("$.[0].menus[0].menuGroupId").exists())
            .andExpect(jsonPath("$.[0].menus[0].categoryId").exists())
            .andExpect(jsonPath("$.[0].menus[0].imageUrl").exists())
            .andExpect(jsonPath("$.[0].menus[0].main").exists())
            .andExpect(jsonPath("$.[0].menus[0].best").exists())
            .andExpect(jsonPath("$.[0].menus[0].price").exists())
        ;
    }

    @Test
    void 메뉴그룹_등록에_성공한다() throws Exception {
        // given
        final MenuGroupRequest request = 식사류_메뉴그룹_요청();

        given(groupService.create(request)).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(
            post(GROUP_BASE_URL)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(groupService).create(any(MenuGroupRequest.class));
        result.andExpect(status().isOk());
    }

    @Test
    void 잘못된_요청시_메뉴그룹_등록에_실패한다() throws Exception {
        // given
        final MenuGroupRequest request = 잘못된_메뉴그룹_요청();

        // when
        ResultActions result = mockMvc.perform(
            post(GROUP_BASE_URL)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(groupService, never()).create(any(MenuGroupRequest.class));
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 메뉴그룹_수정에_성공한다() throws Exception {
        // given
        final long menuGroupId = 1L;
        final MenuGroupRequest request = 식사류_메뉴그룹_요청();

        willDoNothing().given(groupService).update(anyLong(), any(MenuGroupRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            put(GROUP_BASE_URL + "/" + menuGroupId)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(groupService).update(anyLong(), any(MenuGroupRequest.class));
        result.andExpect(status().isOk());
    }

    @Test
    void 잘못된_요청시_메뉴그룹_수정에_실패한다() throws Exception {
        // given
        final long menuGroupId = 1L;
        final MenuGroupRequest request = 잘못된_메뉴그룹_요청();

        // when
        ResultActions result = mockMvc.perform(
            put(GROUP_BASE_URL + "/" + menuGroupId)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(groupService, never()).update(anyLong(), any(MenuGroupRequest.class));
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 메뉴그룹이_존재하지_않는다면_수정에_실패한다() throws Exception {
        // given
        final long menuGroupId = 1L;
        final MenuGroupRequest request = 식사류_메뉴그룹_요청();

        willThrow(new HoneyBreadException(ErrorCode.MENU_GROUP_NOT_FOUND))
            .given(groupService).update(anyLong(), any(MenuGroupRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            put(GROUP_BASE_URL + "/" + menuGroupId)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(groupService).update(anyLong(), any(MenuGroupRequest.class));
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 메뉴그룹이_존재한다면_삭제에_성공한다() throws Exception {
        // given
        final long menuGroupId = 1L;

        willDoNothing().given(service).delete(anyLong());

        // when
        ResultActions result = mockMvc.perform(
            delete(GROUP_BASE_URL + "/" + menuGroupId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).delete(anyLong());
        result.andExpect(status().isNoContent());
    }

    @Test
    void 메뉴그룹이_존재하지_않는다면_삭제에_실패한다() throws Exception {
        // given
        final long menuGroupId = 1L;

        willThrow(new HoneyBreadException(ErrorCode.MENU_GROUP_NOT_FOUND))
            .given(groupService).delete(anyLong());

        // when
        ResultActions result = mockMvc.perform(
            delete(GROUP_BASE_URL + "/" + menuGroupId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(groupService).delete(anyLong());
        result.andExpect(status().isNotFound());
    }

    @Test
    void 벨리데이션_성공시_등록에_성공한다() throws Exception {
        // given
        final MenuRequest request = 간장찜닭_메뉴_요청();

        given(service.create(any(MenuRequest.class))).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(
            post(BASE_URL)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).create(any(MenuRequest.class));
        result.andExpect(status().isCreated());
    }

    @Test
    void 벨리데이션_실패시_등록에_실패한다() throws Exception {
        // given
        final MenuRequest request = 간장찜닭_메뉴_요청();

        willThrow(ValidationException.class).given(service).create(any(MenuRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            post(BASE_URL)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).create(any(MenuRequest.class));
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 잘못된_요청시_등록에_실패한다() throws Exception {
        // given
        final MenuRequest request = 잘못된_메뉴_요청();

        // when
        ResultActions result = mockMvc.perform(
            post(BASE_URL)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service, never()).create(any(MenuRequest.class));
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 벨리데이션_성공시_수정에_성공한다() throws Exception {
        // given
        final Long menuId = 1L;
        final MenuRequest request = 간장찜닭_메뉴_요청();

        willDoNothing().given(service).update(anyLong(), any(MenuRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            put(BASE_URL + "/" + menuId)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).update(anyLong(), any(MenuRequest.class));
        result.andExpect(status().isOk());
    }

    @Test
    void 벨리데이션_실패시_수정에_실패한다() throws Exception {
        // given
        final Long menuId = 1L;
        final MenuRequest request = 간장찜닭_메뉴_요청();

        willThrow(ValidationException.class).given(service).update(anyLong(), any(MenuRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            put(BASE_URL + "/" + menuId)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).update(anyLong(), any(MenuRequest.class));
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 잘못된_요청시_수정에_실패한다() throws Exception {
        // given
        final Long menuId = 1L;
        final MenuRequest request = 잘못된_메뉴_요청();

        // when
        ResultActions result = mockMvc.perform(
            put(BASE_URL + "/" + menuId)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service, never()).update(anyLong(), any(MenuRequest.class));
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 메뉴가_존재하지_않는다면_수정에_실패한다() throws Exception {
        // given
        final Long menuId = 1L;
        final MenuRequest request = 간장찜닭_메뉴_요청();

        willThrow(new HoneyBreadException(ErrorCode.MENU_NOT_FOUND))
            .given(service).update(anyLong(), any(MenuRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            put(BASE_URL + "/" + menuId)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).update(anyLong(), any(MenuRequest.class));
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 메뉴가_존재한다면_삭제에_성공한다() throws Exception {
        // given
        final Long menuId = 1L;

        willDoNothing().given(service).delete(anyLong());

        // when
        ResultActions result = mockMvc.perform(
            delete(BASE_URL + "/" + menuId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).delete(anyLong());
        result.andExpect(status().isNoContent());
    }

    @Test
    void 메뉴가_존재하지_않는다면_삭제에_실패한다() throws Exception {
        // given
        final Long menuId = 1L;

        willThrow(new HoneyBreadException(ErrorCode.MENU_NOT_FOUND)).given(service).delete(anyLong());

        // when
        ResultActions result = mockMvc.perform(
            delete(BASE_URL + "/" + menuId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).delete(anyLong());
        result.andExpect(status().isNotFound());
    }

    private MenuGroupRequest 식사류_메뉴그룹_요청() {
        return MenuGroupRequest.builder()
            .name("식사류")
            .description("식사메뉴 입니다 ~")
            .storeId(1L)
            .build();
    }

    private MenuGroupRequest 잘못된_메뉴그룹_요청() {
        return MenuGroupRequest.builder().build();
    }

    private MenuRequest 간장찜닭_메뉴_요청() {
        return MenuRequest.builder()
            .storeId(1L)
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


    private MenuRequest 잘못된_메뉴_요청() {
        return MenuRequest.builder().build();
    }
}