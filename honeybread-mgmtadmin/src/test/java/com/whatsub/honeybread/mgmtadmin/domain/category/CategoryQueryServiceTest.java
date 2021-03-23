package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.core.domain.category.Category;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.domain.category.dto.CategorySearch;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = CategoryQueryService.class)
@RequiredArgsConstructor
class CategoryQueryServiceTest {

    final CategoryQueryService queryService;

    @MockBean
    CategoryRepository repository;

    @Test
    void 검색_요청시_검색조건이_없다면_SIZE_만큼_조회_성공() {
        // given
        final int size = 10;
        final List<Category> mockCategories = generateMockCategories(size);

        PageRequest pageRequest = PageRequest.of(0, size);
        CategorySearch search = new CategorySearch();

        given(repository.getCategories(any(Pageable.class), any(CategorySearch.class)))
            .willReturn(new PageImpl<>(mockCategories, pageRequest, mockCategories.size()));

        // when
        Page<CategoryResponse> response = queryService.getCategories(pageRequest, search);

        // then
        verify(repository).getCategories(any(Pageable.class), any(CategorySearch.class));

        assertThat(response.getTotalElements()).isEqualTo(size);
        assertThat(response.getContent().size()).isEqualTo(size);
    }

    @Test
    void 검색요청시_검색조건이_있다면_해당하는_목록_조회_성공() {
        // given
        final int size = 10;
        final List<Category> mockCategories = generateMockCategories(size);
        final List<Category> willSearchCategories = mockCategories.subList(1, 2);

        PageRequest pageRequest = PageRequest.of(0, size);
        CategorySearch search = new CategorySearch();
        search.setName("카테고리1");

        given(repository.getCategories(any(Pageable.class), any(CategorySearch.class)))
            .willReturn(new PageImpl<>(willSearchCategories, pageRequest, willSearchCategories.size()));

        // when
        Page<CategoryResponse> response = queryService.getCategories(pageRequest, search);

        // then
        verify(repository).getCategories(any(Pageable.class), any(CategorySearch.class));

        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContent().size()).isEqualTo(1);
    }

    @Test
    void 상세조회_요청시_해당카테고리가_있다면_조회_성공() {
        // given
        final Long categoryId = 1L;
        final String name = "한식";
        final Category mockCategory = mock(Category.class);

        given(mockCategory.getId()).willReturn(categoryId);
        given(mockCategory.getName()).willReturn(name);
        given(repository.findById(anyLong())).willReturn(Optional.of(mockCategory));

        // when
        CategoryResponse response = queryService.getCategory(categoryId);

        // then
        verify(repository).findById(anyLong());

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(categoryId);
        assertThat(response.getName()).isEqualTo(name);
    }

    @Test
    void 상세조회_요청시_해당카테고리가_없다면_예외_발생() {
        // given
        final Long categoryId = 1L;

        given(repository.findById(anyLong())).willReturn(Optional.empty());

        // when
        HoneyBreadException ex = assertThrows(
            HoneyBreadException.class,
            () -> queryService.getCategory(categoryId)
        );

        // then
        verify(repository).findById(anyLong());

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
    }

    private List<Category> generateMockCategories(final int size) {
        return LongStream.range(0, size)
            .mapToObj(id -> {
                Category mock = mock(Category.class);
                given(mock.getId()).willReturn(id);
                given(mock.getName()).willReturn("카테고리" + id);
                return mock;
            })
            .collect(Collectors.toList());
    }
}