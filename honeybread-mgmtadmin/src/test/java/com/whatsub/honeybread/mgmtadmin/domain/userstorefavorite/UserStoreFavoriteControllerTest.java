package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.dto.StoreIdsResponse;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(UserStoreFavoriteController.class)
@RequiredArgsConstructor
class UserStoreFavoriteControllerTest {
    static final String BASE_URL = "/stores/favorites";
    static final String CREATE_URL = BASE_URL + "/" + 1L;
    static final String DELETE_URL = BASE_URL + "/" + 1L;

    private final MockMvc mockMvc;

    @MockBean
    UserStoreFavoriteService service;

    @MockBean
    UserStoreFavoriteQueryService queryService;

    @Test
    void 목록_조회_성공() throws Exception {
        // given
        final int 찜_개수 = 5;
        final int 스토어_아이디_시작값 = 2;
        Collection<Long> 찜한_스토어_아이디_목록 = 찜한_스토어_아이디_목록(스토어_아이디_시작값, 찜_개수);
        given(queryService.getStoresByUserId(anyLong()))
                .willReturn(new StoreIdsResponse(찜한_스토어_아이디_목록));

        // when
        ResultActions 조회_결과 = mockMvc.perform(
                get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(queryService).getStoresByUserId(anyLong());

        조회_결과.andExpect(status().isOk())
                .andExpect(jsonPath("$.storeIds").isNotEmpty())
                .andExpect(jsonPath("$.storeIds.length()").value(찜한_스토어_아이디_목록.size()))
                .andExpect(jsonPath("$.storeIds[0]").value(2))
                .andExpect(jsonPath("$.storeIds[1]").value(3))
                .andExpect(jsonPath("$.storeIds[2]").value(4))
                .andExpect(jsonPath("$.storeIds[3]").value(5))
                .andExpect(jsonPath("$.storeIds[4]").value(6))
                .andExpect(jsonPath("$.storeIds[5]").doesNotExist())
        ;
    }

    private List<Long> 찜한_스토어_아이디_목록(final int startStoreId, final int size) {
        return LongStream.range(startStoreId, startStoreId + size)
                .boxed()
                .collect(Collectors.toList());
    }

    @Test
    void 존재하지_않는_스토어면_등록_실패() throws Exception {
        // given
        given(service.create(anyLong(), anyLong())).willThrow(new HoneyBreadException(ErrorCode.STORE_NOT_FOUND));

        // when
        ResultActions response = 찜등록_요청();

        // then
        verify(service).create(anyLong(), anyLong());
        response.andExpect(status().isNotFound());
    }

    @Test
    void 존재하는_스토어_그리고_찜한적_없으면_등록_성공() throws Exception {
        // given
        given(service.create(anyLong(), anyLong())).willReturn(Long.MIN_VALUE);

        // when
        ResultActions response = 찜등록_요청();

        // then
        verify(service).create(anyLong(), anyLong());
        response.andExpect(status().isCreated());
    }

    @Test
    void 존재하는_스토어지만_이미_찜했기에_등록_실패() throws Exception {
        // given
        given(service.create(anyLong(), anyLong())).willThrow(new HoneyBreadException(ErrorCode.DUPLICATE_USER_STORE_FAVORITE));

        // when
        ResultActions response = 찜등록_요청();

        // then
        verify(service).create(anyLong(), anyLong());
        response.andExpect(status().isConflict());
    }

    @Test
    void 존재하지_않는_스토어면_삭제_실패() throws Exception {
        // given
        given(service.delete(anyLong(), anyLong())).willThrow(new HoneyBreadException(ErrorCode.STORE_NOT_FOUND));

        // when
        ResultActions response = 찜삭제_요청();

        // then
        verify(service).delete(anyLong(), anyLong());
        response.andExpect(status().isNotFound());
    }

    @Test
    void 찜한적_없어서_삭제_실패() throws Exception {
        // given
        given(service.delete(anyLong(), anyLong())).willThrow(new HoneyBreadException(ErrorCode.USER_STORE_FAVORITE_NOT_FOUND));

        // when
        ResultActions response = 찜삭제_요청();

        // then
        verify(service).delete(anyLong(), anyLong());
        response.andExpect(status().isNotFound());
    }

    @Test
    void 존재하는_스토어_그리고_찜했으면_삭제_성공() throws Exception {
        // given
        given(service.delete(anyLong(), anyLong())).willReturn(Long.MIN_VALUE);

        // when
        ResultActions response = 찜삭제_요청();

        // then
        verify(service).delete(anyLong(), anyLong());
        response.andExpect(status().isNoContent());
    }

    private ResultActions 찜등록_요청() throws Exception {
        return mockMvc.perform(
                post(CREATE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    private ResultActions 찜삭제_요청() throws Exception {
        return mockMvc.perform(
                delete(DELETE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

}