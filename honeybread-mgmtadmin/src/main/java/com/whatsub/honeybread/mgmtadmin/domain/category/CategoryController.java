package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.category.dto.CategoryRequest;
import com.whatsub.honeybread.mgmtadmin.domain.category.dto.CategoryResponse;
import com.whatsub.honeybread.mgmtadmin.support.MgmtAdminSwaggerTags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
class CategoryController {
    private final CategoryService service;
    private final CategoryQueryService queryService;

    // 목록 조회
    @ApiOperation(
        value = "카테고리 목록 조회",
        tags = MgmtAdminSwaggerTags.CATEGORY
    )
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(@RequestParam List<Long> categoryIds) {
        List<CategoryResponse> response = queryService.getCategories(categoryIds);
        return ResponseEntity.ok(response);
    }

    // 상세 조회
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
    public ResponseEntity createCategory(@Valid @RequestBody CategoryRequest request, BindingResult result) {
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
    public ResponseEntity updateCategory(
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
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}