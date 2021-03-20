package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.core.domain.category.Category;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    // 수정

    // 삭제

    private CategoryRequest generateRequest(String name) {
        return new CategoryRequest(name);
    }
}