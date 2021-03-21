package com.whatsub.honeybread.mgmtadmin.domain.category.dto;

import com.whatsub.honeybread.core.domain.category.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CategoryResponse {
    Long id;
    String name;

    public static CategoryResponse of(Category entity) {
        return new CategoryResponse(entity.getId(), entity.getName());
    }
}
