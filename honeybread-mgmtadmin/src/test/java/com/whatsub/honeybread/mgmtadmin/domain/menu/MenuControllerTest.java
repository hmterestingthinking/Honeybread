package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import com.whatsub.honeybread.core.domain.menu.validator.MenuValidator;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(MenuController.class)
@RequiredArgsConstructor
class MenuControllerTest {

    static final String BASE_URL = "/menus";

    final MockMvc mockMvc;

    final ObjectMapper mapper;

    @MockBean
    MenuService service;

    @Test
    void 벨리데이션_성공시_메뉴_등록_성공() throws Exception {
        // given
        final MenuRequest request = 간장찜닭_메뉴_생성_요청();

        given(service.create(any(MenuRequest.class))).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(
            post(BASE_URL)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result.andExpect(status().isCreated());
    }

    @Test
    void 벨리데이션_실패시_메뉴_등록_실패() throws Exception {
        // given
        final MenuRequest request = 간장찜닭_메뉴_생성_요청();

        willThrow(ValidationException.class).given(service).create(any(MenuRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            post(BASE_URL)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result.andExpect(status().is4xxClientError());
    }

    private MenuRequest 간장찜닭_메뉴_생성_요청() {
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