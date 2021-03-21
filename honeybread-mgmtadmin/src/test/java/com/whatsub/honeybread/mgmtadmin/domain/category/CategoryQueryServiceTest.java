package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.core.domain.category.Category;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CategoryQueryService.class)
class CategoryQueryServiceTest {

    @Autowired
    CategoryQueryService queryService;

    @MockBean
    CategoryRepository repository;

    // 목록 조회

    // 상세 조회
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
}