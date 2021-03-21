package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.core.domain.category.Category;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
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
    void 목록_조회_요청시_존재하는_카테고리의_수만큼_조회_성공() {
        // given
        final List<Long> categoryIds = LongStream.range(1, 10)
            .boxed()
            .collect(Collectors.toList());
        final List<Category> mockCategories = generateMockCategories(categoryIds);

        given(repository.findAllById(anyList())).willReturn(mockCategories);

        // when
        List<CategoryResponse> response = queryService.getCategories(categoryIds);

        // then
        verify(repository).findAllById(anyList());

        assertThat(CollectionUtils.isEmpty(response)).isFalse();
        assertThat(response.size()).isEqualTo(categoryIds.size());
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

    private List<Category> generateMockCategories(List<Long> categoryIds) {
        return categoryIds.stream()
            .map(id -> {
                Category mock = mock(Category.class);

                given(mock.getId()).willReturn(id);
                given(mock.getName()).willReturn("카테고리" + id);
                return mock;
            })
            .collect(Collectors.toList());
    }
}