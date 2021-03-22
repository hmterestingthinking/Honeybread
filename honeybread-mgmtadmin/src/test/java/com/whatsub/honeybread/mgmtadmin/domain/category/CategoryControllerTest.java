package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.category.dto.CategorySearch;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.category.dto.CategoryRequest;
import com.whatsub.honeybread.mgmtadmin.domain.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(CategoryController.class)
@RequiredArgsConstructor
class CategoryControllerTest {

    static final String BASE_URL = "/categories";

    final MockMvc mockMvc;

    final ObjectMapper mapper;

    @MockBean
    CategoryService service;

    @MockBean
    CategoryQueryService queryService;

    @Test
    void 검색_요청시_검색조건이_없다면_SIZE_만큼_조회에_성공한다() throws Exception {
        // given
        final int size = 10;
        final List<CategoryResponse> mockCategories = generateMockCategories(size);

        PageRequest pageRequest = PageRequest.of(0, size);

        given(queryService.getCategories(any(Pageable.class), any(CategorySearch.class)))
            .willReturn(new PageImpl<>(mockCategories, pageRequest, mockCategories.size()));

        // when
        ResultActions result = mockMvc.perform(
            get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(queryService).getCategories(any(Pageable.class), any(CategorySearch.class));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isNotEmpty())
            .andExpect(jsonPath("$.content.length()").value(size))
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.content[0].name").exists());
    }

    @Test
    void 검색_요청시_검색조건이_있다면_해당하는_목록_조회에_성공한다() throws Exception {
        // given
        final int size = 10;
        final List<CategoryResponse> mockCategories = generateMockCategories(size);
        final List<CategoryResponse> willSearchCategories = mockCategories.subList(1, 2);

        PageRequest pageRequest = PageRequest.of(0, size);
        CategorySearch search = new CategorySearch();
        search.setName("카테고리1");

        given(queryService.getCategories(any(Pageable.class), any(CategorySearch.class)))
            .willReturn(new PageImpl<>(willSearchCategories, pageRequest, willSearchCategories.size()));

        // when
        ResultActions result = mockMvc.perform(
            get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(queryService).getCategories(any(Pageable.class), any(CategorySearch.class));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isNotEmpty())
            .andExpect(jsonPath("$.content.length()").value(willSearchCategories.size()))
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.content[0].name").value(search.getName()));
    }

    @Test
    void 카테고리가_존재한다면_조회에_성공한다() throws Exception {
        // given
        final Long categoryId = 1L;

        CategoryResponse 카테고리_응답 = CategoryResponse.builder()
            .id(categoryId)
            .name("한식")
            .build();

        given(queryService.getCategory(anyLong())).willReturn(카테고리_응답);

        // when
        ResultActions result = mockMvc.perform(
            get(BASE_URL + "/" + categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(queryService).getCategory(anyLong());

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(카테고리_응답.getId()))
            .andExpect(jsonPath("$.name").value(카테고리_응답.getName()));
    }

    @Test
    void 카테고리가_존재하지_않는다면_조회에_실패한다() throws Exception {
        // given
        final Long categoryId = 1L;

        doAnswer(mock -> {
            throw new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND);
        }).when(queryService).getCategory(anyLong());

        // when
        ResultActions result = mockMvc.perform(
            get(BASE_URL + "/" + categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(queryService).getCategory(anyLong());

        result.andExpect(status().isNotFound());
    }

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

    @Test
    void 카테고리가_존재할경우_삭제에_성공한다() throws Exception {
        // given
        final Long categoryId = 1L;

        doNothing().when(service).delete(anyLong());

        // when
        ResultActions result = mockMvc.perform(
            delete(BASE_URL + "/" + categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).delete(anyLong());

        result.andExpect(status().isOk());
    }

    @Test
    void 카테고리가_존재하지_않을경우_삭제에_실패한다() throws Exception {
        // given
        final Long categoryId = 1L;

        doAnswer(mock -> {
            throw new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND);
        }).when(service).delete(anyLong());

        // when
        ResultActions result = mockMvc.perform(
            delete(BASE_URL + "/" + categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        verify(service).delete(anyLong());

        result.andExpect(status().isNotFound());
    }

    private CategoryRequest generateRequest(String name) {
        return new CategoryRequest(name);
    }

    private List<CategoryResponse> generateMockCategories(final int size) {
        return LongStream.range(0, size)
            .mapToObj(id -> CategoryResponse.builder()
                .id(id)
                .name("카테고리" + id)
                .build())
            .collect(Collectors.toList());
    }
}