package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {
    private final CategoryRepository repository;

    // 목록 조회
    public List<CategoryResponse> getCategories(final List<Long> categoryIds) {
        return repository.findAllById(categoryIds)
            .stream()
            .map(CategoryResponse::of)
            .collect(Collectors.toList());
    }

    // 상세 조회
    public CategoryResponse getCategory(final Long categoryId) {
        return repository.findById(categoryId)
            .map(CategoryResponse::of)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
