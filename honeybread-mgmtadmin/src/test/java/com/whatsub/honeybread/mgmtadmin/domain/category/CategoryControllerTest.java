package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    static final String BASE_URL = "/categories";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CategoryService service;


    // 등록
    @Test
    void 중복되는_카테고리가_없다면_등록에_성공한다() throws Exception {
        // given
        CategoryRequest 카테고리_생성_요청 = generateRequest("한식");

        given(service.create(any(CategoryRequest.class))).willReturn(Long.MIN_VALUE);

        // when
        ResultActions result = mockMvc.perform(
            post(BASE_URL)
                .content(mapper.writeValueAsString(카테고리_생성_요청))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).create(any(CategoryRequest.class));

        result.andExpect(status().isCreated());
    }

    @Test
    void 중복되는_카테고리가_있다면_등록에_실패한다() throws Exception {
        // given
        CategoryRequest 카테고리_생성_요청 = generateRequest("한식");

        given(service.create(any(CategoryRequest.class))).willAnswer(mock -> {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_CATEGORY);
        });

        // when
        ResultActions result = mockMvc.perform(
            post(BASE_URL)
                .content(mapper.writeValueAsString(카테고리_생성_요청))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).create(any(CategoryRequest.class));

        result.andExpect(status().is4xxClientError());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "한",
        "한식중식양식일식abcdefghijklmnopqrstuvwxyz123456789010가나다라마바사아자차카"
    })
    void 카테고리명이_2글자_미만_50자초과라면_등록에_실패한다(String name) throws Exception {
        // given
        CategoryRequest 카테고리_생성_요청 = generateRequest(name);

        // when
        ResultActions result = mockMvc.perform(
            post(BASE_URL)
                .content(mapper.writeValueAsString(카테고리_생성_요청))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result.andExpect(status().is4xxClientError());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "한식",
        "한식123",
        "한식12345678910",
        "한식중식양식일식abcdefghijklmnopqrstuvwxyz123456789010가나다"
    })
    void 카테고리명이_2글자_이상_50자_이하라면_등록에_성공한다(String name) throws Exception {
        // given
        CategoryRequest 카테고리_생성_요청 = generateRequest(name);

        given(service.create(any(CategoryRequest.class))).willReturn(Long.MIN_VALUE);

        // when
        ResultActions result = mockMvc.perform(
            post(BASE_URL)
                .content(mapper.writeValueAsString(카테고리_생성_요청))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).create(any(CategoryRequest.class));

        result.andExpect(status().isCreated());
    }

    // 수정
    @Test
    void 중복되는_카테고리가_없다면_수정에_성공한다() throws Exception {
        // given
        final Long categoryId = 1L;
        CategoryRequest 카테고리_수정_요청 = generateRequest("한식을 중식으로 수정");

        doNothing().when(service).update(anyLong(), any(CategoryRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            put(BASE_URL + "/" + categoryId)
                .content(mapper.writeValueAsString(카테고리_수정_요청))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).update(anyLong(), any(CategoryRequest.class));

        result.andExpect(status().isOk());
    }

    @Test
    void 중복되는_카테고리가_있다면_수정에_실패한다() throws Exception {
        // given
        final Long categoryId = 1L;
        CategoryRequest 카테고리_수정_요청 = generateRequest("한식을 중식으로 수정");

        doAnswer(mock -> {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_CATEGORY);
        }).when(service).update(anyLong(), any(CategoryRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            put(BASE_URL + "/" + categoryId)
                .content(mapper.writeValueAsString(카테고리_수정_요청))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).update(anyLong(), any(CategoryRequest.class));

        result.andExpect(status().isConflict());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "한",
        "한식중식양식일식abcdefghijklmnopqrstuvwxyz123456789010가나다라마바사아자차카"
    })
    void 카테고리명이_2글자_미만_50자_초과라면_수정에_실패한다(String name) throws Exception {
        // given
        final Long categoryId = 1L;
        CategoryRequest 카테고리_수정_요청 = generateRequest(name);

        // when
        ResultActions result = mockMvc.perform(
            put(BASE_URL + "/" + categoryId)
                .content(mapper.writeValueAsString(카테고리_수정_요청))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result.andExpect(status().is4xxClientError());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "한식",
        "한식123",
        "한식12345678910",
        "한식중식양식일식abcdefghijklmnopqrstuvwxyz123456789010가나다"
    })
    void 카테고리명이_2글자_이상_50자_이하라면_수정에_성공한다(String name) throws Exception {
        // given
        final Long categoryId = 1L;
        CategoryRequest 카테고리_수정_요청 = generateRequest(name);

        doNothing().when(service).update(anyLong(), any(CategoryRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            put(BASE_URL + "/" + categoryId)
                .content(mapper.writeValueAsString(카테고리_수정_요청))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).update(anyLong(), any(CategoryRequest.class));

        result.andExpect(status().isOk());
    }

    @Test
    void 카테고리가_존재하지_않는다면_수정에_실패한다() throws Exception {
        // given
        final Long categoryId = 1L;
        CategoryRequest 카테고리_수정_요청 = generateRequest("한식에서 중식으로 수정");

        doAnswer(mock -> {
            throw new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND);
        }).when(service).update(anyLong(), any(CategoryRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            put(BASE_URL + "/" + categoryId)
                .content(mapper.writeValueAsString(카테고리_수정_요청))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).update(anyLong(), any(CategoryRequest.class));

        result.andExpect(status().isNotFound());
    }


    // 삭제

    private CategoryRequest generateRequest(String name) {
        return new CategoryRequest(name);
    }
}