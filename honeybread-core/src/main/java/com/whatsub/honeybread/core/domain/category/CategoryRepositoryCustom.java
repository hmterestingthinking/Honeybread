package com.whatsub.honeybread.core.domain.category;

import com.whatsub.honeybread.core.domain.category.dto.CategorySearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepositoryCustom {
    Page<Category> getCategories(Pageable pageable, CategorySearch search);
}
