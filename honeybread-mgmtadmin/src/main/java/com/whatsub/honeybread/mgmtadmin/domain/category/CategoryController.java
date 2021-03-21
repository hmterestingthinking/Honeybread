package com.whatsub.honeybread.mgmtadmin.domain.category;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.support.MgmtAdminSwaggerTags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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

    // 목록 조회

    // 상세 조회

    // 등록
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

    // 수정

    // 삭제
}
