package com.whatsub.honeybread.mgmtadmin.domain.category;

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

    // 등록
    @Transactional
    public Long create(final CategoryRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_CATEGORY);
        }
        return repository.save(request.toEntity()).getId();
    }

    // 수정

    // 삭제
}
