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
        스토어_메뉴목록_조회시_성공한다();

        // when
        ResultActions result = mockMvc.perform(
            get(BASE_URL + "/stores/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        then(queryRepository).should().findAllByStoreId(anyLong());

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
        ResultActions result = 메뉴그룹_등록(request);

        // then
        then(groupService).should().create(any(MenuGroupRequest.class));
        Created_응답_확인(result);
    }

    @Test
    void 잘못된_요청시_메뉴그룹_등록에_실패한다() throws Exception {
        // when
        ResultActions result = 메뉴그룹_등록(잘못된_메뉴그룹_요청());

        // then
        then(groupService).should(never()).create(any(MenuGroupRequest.class));
        BadRequest_응답_확인(result);
    }

    @Test
    void 메뉴그룹_수정에_성공한다() throws Exception {
        // given
        willDoNothing().given(groupService).update(anyLong(), any(MenuGroupRequest.class));

        // when
        ResultActions result = 메뉴그룹_수정(식사류_메뉴그룹_요청());

        // then
        메뉴그룹_수정이_수행되어야_한다();
        Ok_응답_확인(result);
    }

    @Test
    void 메뉴그룹이_존재하지_않는다면_수정에_실패한다() throws Exception {
        // given
        willThrow(new HoneyBreadException(ErrorCode.MENU_GROUP_NOT_FOUND))
            .given(groupService).update(anyLong(), any(MenuGroupRequest.class));

        // when
        ResultActions result = 메뉴그룹_수정(식사류_메뉴그룹_요청());

        // then
        메뉴그룹_수정이_수행되어야_한다();
        BadRequest_응답_확인(result);
    }

    @Test
    void 잘못된_요청시_메뉴그룹_수정에_실패한다() throws Exception {
        // when
        ResultActions result = 메뉴그룹_수정(잘못된_메뉴그룹_요청());

        // then
        then(groupService).should(never()).update(anyLong(), any(MenuGroupRequest.class));
        BadRequest_응답_확인(result);
    }

    @Test
    void 메뉴그룹이_존재한다면_삭제에_성공한다() throws Exception {
        // given
        willDoNothing().given(service).delete(anyLong());

        // when
        ResultActions result = 메뉴그룹_삭제();

        // then
        메뉴그룹_삭제가_수행되어야_한다();
        NoContent_응답_확인(result);
    }

    @Test
    void 메뉴그룹이_존재하지_않는다면_삭제에_실패한다() throws Exception {
        // given
        willThrow(new HoneyBreadException(ErrorCode.MENU_GROUP_NOT_FOUND))
            .given(groupService).delete(anyLong());

        // when
        ResultActions result = 메뉴그룹_삭제();

        // then
        메뉴그룹_삭제가_수행되어야_한다();
        result.andExpect(status().isNotFound());
    }

    @Test
    void 벨리데이션_성공시_등록에_성공한다() throws Exception {
        // given
        given(service.create(any(MenuRequest.class))).willReturn(1L);

        // when
        ResultActions result = 메뉴_등록(간장찜닭_메뉴_요청());

        // then
        메뉴_등록이_수행되어야_한다();
        Created_응답_확인(result);
    }

    @Test
    void 벨리데이션_실패시_등록에_실패한다() throws Exception {
        // given
        willThrow(ValidationException.class).given(service).create(any(MenuRequest.class));

        // when
        ResultActions result = 메뉴_등록(간장찜닭_메뉴_요청());

        // then
        메뉴_등록이_수행되어야_한다();
        BadRequest_응답_확인(result);
    }

    @Test
    void 잘못된_요청시_등록에_실패한다() throws Exception {
        // when
        ResultActions result = 메뉴_등록(잘못된_메뉴_요청());

        // then
        then(service).should(never()).create(any(MenuRequest.class));
        BadRequest_응답_확인(result);
    }

    @Test
    void 벨리데이션_성공시_수정에_성공한다() throws Exception {
        // given
        willDoNothing().given(service).update(anyLong(), any(MenuRequest.class));

        // when
        ResultActions result = 메뉴_수정(간장찜닭_메뉴_요청());

        // then
        메뉴_수정이_수행되어야_한다();
        Ok_응답_확인(result);
    }

    @Test
    void 벨리데이션_실패시_수정에_실패한다() throws Exception {
        // given
        willThrow(ValidationException.class).given(service).update(anyLong(), any(MenuRequest.class));

        // when
        ResultActions result = 메뉴_수정(간장찜닭_메뉴_요청());

        // then
        메뉴_수정이_수행되어야_한다();
        BadRequest_응답_확인(result);
    }

    @Test
    void 잘못된_요청시_수정에_실패한다() throws Exception {
        // when
        ResultActions result = 메뉴_수정(잘못된_메뉴_요청());

        // then
        then(service).should(never()).update(anyLong(), any(MenuRequest.class));
        BadRequest_응답_확인(result);
    }

    @Test
    void 메뉴가_존재하지_않는다면_수정에_실패한다() throws Exception {
        // given
        willThrow(new HoneyBreadException(ErrorCode.MENU_NOT_FOUND))
            .given(service).update(anyLong(), any(MenuRequest.class));

        // when
        ResultActions result = 메뉴_수정(간장찜닭_메뉴_요청());

        // then
        메뉴_수정이_수행되어야_한다();
        BadRequest_응답_확인(result);
    }

    @Test
    void 메뉴가_존재한다면_삭제에_성공한다() throws Exception {
        // given
        willDoNothing().given(service).delete(anyLong());

        // when
        ResultActions result = 메뉴_삭제();

        // then
        메뉴_삭제가_수행되어야_한다();
        NoContent_응답_확인(result);
    }

    @Test
    void 메뉴가_존재하지_않는다면_삭제에_실패한다() throws Exception {
        // given
        willThrow(new HoneyBreadException(ErrorCode.MENU_NOT_FOUND)).given(service).delete(anyLong());

        // when
        ResultActions result = 메뉴_삭제();

        // then
        메뉴_삭제가_수행되어야_한다();
        result.andExpect(status().isNotFound());
    }

    /**
     * Given
     */
    private void 스토어_메뉴목록_조회시_성공한다() {
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
    }

    /**
     * When
     */
    private ResultActions 메뉴그룹_등록(MenuGroupRequest request) throws Exception {
        return mockMvc.perform(
            post(GROUP_BASE_URL)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    private ResultActions 메뉴그룹_수정(MenuGroupRequest request) throws Exception {
        return mockMvc.perform(
            put(GROUP_BASE_URL + "/1")
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    private ResultActions 메뉴그룹_삭제() throws Exception {
        return mockMvc.perform(
            delete(GROUP_BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    private ResultActions 메뉴_등록(MenuRequest request) throws Exception {
        return mockMvc.perform(
            post(BASE_URL)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    private ResultActions 메뉴_수정(MenuRequest request) throws Exception {
        return mockMvc.perform(
            put(BASE_URL + "/1")
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    private ResultActions 메뉴_삭제() throws Exception {
        return mockMvc.perform(
            delete(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    /**
     * Then
     */
    private void 메뉴그룹_수정이_수행되어야_한다() {
        then(groupService).should().update(anyLong(), any(MenuGroupRequest.class));
    }

    private void 메뉴그룹_삭제가_수행되어야_한다() {
        then(groupService).should().delete(anyLong());
    }

    private void 메뉴_등록이_수행되어야_한다() {
        then(service).should().create(any(MenuRequest.class));
    }

    private void 메뉴_수정이_수행되어야_한다() {
        then(service).should().update(anyLong(), any(MenuRequest.class));
    }

    private void 메뉴_삭제가_수행되어야_한다() {
        then(service).should().delete(anyLong());
    }

    private void Ok_응답_확인(ResultActions result) throws Exception {
        result.andExpect(status().isOk());
    }

    private void Created_응답_확인(ResultActions result) throws Exception {
        result.andExpect(status().isCreated());
    }

    private void NoContent_응답_확인(ResultActions result) throws Exception {
        result.andExpect(status().isNoContent());
    }

    private void BadRequest_응답_확인(ResultActions result) throws Exception {
        result.andExpect(status().is4xxClientError());
    }


    /**
     * Helper
     */
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