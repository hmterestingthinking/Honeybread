package com.whatsub.honeybread.core.domain.category;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.whatsub.honeybread.core.domain.category.dto.CategorySearch;
import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.whatsub.honeybread.core.domain.category.QCategory.category;

public class CategoryRepositoryImpl extends QuerydslRepositorySupport implements CategoryRepositoryCustom {

    public CategoryRepositoryImpl() {
        super(Category.class);
    }

    @Override
    public Page<Category> getCategories(final Pageable pageable, final CategorySearch search) {
        return applyPagination(pageable,
            query -> query.from(category)
                .where(
                    likeName(search.getName())
                )
        );
    }

    private BooleanExpression likeName(final String name) {
        return StringUtils.isBlank(name) ? null : category.name.like(name);
    }
}
