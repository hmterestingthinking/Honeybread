package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.core.domain.category.Category;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class CategoryService {
    private final CategoryRepository repository;

    @Transactional
    public Long create(final CategoryRequest request) {
        validateDuplicateCategory(request);

        return repository.save(request.toEntity()).getId();
    }

    @Transactional
    public void update(final Long id, final CategoryRequest request) {
        Category findCategory = findCategory(id);

        validateDuplicateCategory(request);

        findCategory.update(request.toEntity());
    }

    @Transactional
    public void delete(final Long id) {
        repository.delete(findCategory(id));
    }

    private void validateDuplicateCategory(CategoryRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_CATEGORY);
        }
    }

    private Category findCategory(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
