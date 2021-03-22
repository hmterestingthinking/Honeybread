package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.domain.category.dto.CategorySearch;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.category.dto.CategoryRequest;
import com.whatsub.honeybread.mgmtadmin.domain.category.dto.CategoryResponse;
import com.whatsub.honeybread.mgmtadmin.support.MgmtAdminSwaggerTags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
class CategoryController {
    private final CategoryService service;
    private final CategoryQueryService queryService;

    @ApiOperation(
        value = "카테고리 목록 조회",
        tags = MgmtAdminSwaggerTags.CATEGORY
    )
    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getCategories(
        @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt")
        Pageable pageable,
        CategorySearch search
    ) {
        Page<CategoryResponse> response = queryService.getCategories(pageable, search);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(
        value = "카테고리 조회",
        tags = MgmtAdminSwaggerTags.CATEGORY
    )
    @GetMapping("{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        CategoryResponse response = queryService.getCategory(id);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(
        value = "카테고리 등록",
        tags = MgmtAdminSwaggerTags.CATEGORY
    )
    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(
        value = "카테고리 수정",
        tags = MgmtAdminSwaggerTags.CATEGORY
    )
    @PutMapping("{id}")
    public ResponseEntity<Void> updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody CategoryRequest request,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.update(id, request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(
        value = "카테고리 삭제",
        tags = MgmtAdminSwaggerTags.CATEGORY
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
