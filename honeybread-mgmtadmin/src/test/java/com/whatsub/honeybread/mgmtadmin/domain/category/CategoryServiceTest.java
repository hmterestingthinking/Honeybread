package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.core.domain.category.Category;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = CategoryService.class)
@RequiredArgsConstructor
class CategoryServiceTest {
    final CategoryService service;

    @MockBean
    CategoryRepository repository;

    @Test
    void 중복되는_카테고리가_없다면_등록_성공() {
        // given
        CategoryRequest 카테고리_생성_요청 = generateRequest("한식");

        given(repository.existsByName(anyString())).willReturn(false);
        given(repository.save(any(Category.class))).willReturn(mock(Category.class));

        // when
        service.create(카테고리_생성_요청);

        // then
        verify(repository).existsByName(anyString());
        verify(repository).save(any(Category.class));
    }

    @Test
    void 중복되는_카테고리가_있다면_예외_발생() {
        // given
        CategoryRequest 카테고리_생성_요청 = generateRequest("한식");

        given(repository.existsByName(anyString())).willReturn(true);

        // when
        HoneyBreadException ex = assertThrows(
            HoneyBreadException.class,
            () -> service.create(카테고리_생성_요청)
        );

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_CATEGORY);
    }

    @Test
    void 수정요청시_중복되는_카테고리가_없다면_수정_성공() {
        // given
        final Long categoryId = 1L;
        final Category mockCategory = mock(Category.class);

        CategoryRequest 카테고리_수정_요청 = generateRequest("한식을 중식으로 수정");

        given(repository.findById(anyLong())).willReturn(Optional.of(mockCategory));
        given(repository.existsByName(anyString())).willReturn(false);

        // when
        service.update(categoryId, 카테고리_수정_요청);

        // then
        verify(repository).findById(anyLong());
        verify(repository).existsByName(anyString());
        verify(mockCategory).update(any(Category.class));
    }

    @Test
    void 수정요청시_중복되는_카테고리가_있다면_예외_발생() {
        // given
        final Long categoryId = 1L;
        final Category mockCategory = mock(Category.class);

        CategoryRequest 카테고리_수정_요청 = generateRequest("한식을 중식으로 수정");

        given(repository.findById(anyLong())).willReturn(Optional.of(mockCategory));
        given(repository.existsByName(anyString())).willReturn(true);

        // when
        HoneyBreadException ex = assertThrows(
            HoneyBreadException.class,
            () -> service.update(categoryId, 카테고리_수정_요청)
        );

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_CATEGORY);
    }

    @Test
    void 수정요청시_해당카테고리가_없다면_예외_발생() {
        // given
        final Long categoryId = 1L;

        CategoryRequest 카테고리_수정_요청 = generateRequest("한식을 중식으로 수정");

        given(repository.findById(anyLong())).willReturn(Optional.empty());

        // when
        HoneyBreadException ex = assertThrows(
            HoneyBreadException.class,
            () -> service.update(categoryId, 카테고리_수정_요청)
        );

        // then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 삭제요청시_해당카테고리가_있다면_삭제_성공() {
        // given
        final Long categoryId = 1L;
        final Category mockCategory = mock(Category.class);

        given(repository.findById(anyLong())).willReturn(Optional.of(mockCategory));

        // when
        service.delete(categoryId);

        // then
        verify(repository).findById(anyLong());
        verify(repository).delete(any(Category.class));
    }

    @Test
    void 삭제요청시_해당카테고리가_없다면_예외_발생() {
        // given
        final Long categoryId = 1L;

        given(repository.findById(anyLong())).willReturn(Optional.empty());

        // when
        HoneyBreadException ex = assertThrows(
            HoneyBreadException.class,
            () -> service.delete(categoryId)
        );

        // then
        verify(repository).findById(anyLong());

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
    }

    private CategoryRequest generateRequest(String name) {
        return new CategoryRequest(name);
    }
}