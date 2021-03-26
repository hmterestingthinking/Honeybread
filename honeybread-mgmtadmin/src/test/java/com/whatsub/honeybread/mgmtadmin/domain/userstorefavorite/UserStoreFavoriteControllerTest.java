package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.dto.StoreResponse;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(UserStoreFavoriteController.class)
class UserStoreFavoriteControllerTest {
    static final String BASE_URL = "/stores/favorites";
    static final String CREATE_URL = BASE_URL + "/" + 1L;
    static final String DELETE_URL = BASE_URL + "/" + 1L;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserStoreFavoriteService service;

    @MockBean
    UserStoreFavoriteQueryService queryService;

    @Test
    void 목록_조회_성공() throws Exception {
        // given
        final int size = 10;
        final List<StoreResponse> 스토어_목록 = 스토어_목록(size);
        final List<StoreResponse> 조회될_스토어_목록 = 스토어_목록.subList(1, 2);

        PageRequest pageRequest = PageRequest.of(0, size);

        given(queryService.getStoresByUserId(any(Pageable.class), anyLong()))
                .willReturn(new PageImpl<>(조회될_스토어_목록, pageRequest, 조회될_스토어_목록.size()));

        // when
        ResultActions 조회_결과 = mockMvc.perform(
                get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(queryService).getStoresByUserId(any(Pageable.class), anyLong());

        조회_결과.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(조회될_스토어_목록.size()))
                .andExpect(jsonPath("$.content[0].storeId").exists());
    }

    private List<StoreResponse> 스토어_목록(final int size) {
        return LongStream.range(0, size)
                .mapToObj(id -> {
                    Store mock = mock(Store.class);
                    given(mock.getId()).willReturn(id);
                    return StoreResponse.toDto(mock);
                })
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