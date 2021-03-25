package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.domain.category.dto.CategorySearch;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {
    private final CategoryRepository repository;

    public Page<CategoryResponse> getCategories(final Pageable pageable, final CategorySearch search) {
        return repository.getCategories(pageable, search)
            .map(CategoryResponse::of);
    }

    public CategoryResponse getCategory(final Long categoryId) {
        return repository.findById(categoryId)
            .map(CategoryResponse::of)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
