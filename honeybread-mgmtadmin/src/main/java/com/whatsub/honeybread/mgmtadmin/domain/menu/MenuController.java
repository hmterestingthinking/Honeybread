package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuGroupRequest;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuRequest;
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
@RequestMapping("menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService service;
    private final MenuGroupService groupService;

    // 메뉴 그룹 등록
    @ApiOperation(
        value = "메뉴 그룹 등록",
        tags = MgmtAdminSwaggerTags.MENU
    )
    @PostMapping("groups")
    public ResponseEntity<Void> createMenuGroups(@Valid @RequestBody MenuGroupRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        groupService.create(request);
        return ResponseEntity.ok().build();
    }

    // 메뉴 그룹 수정
    @ApiOperation(
        value = "메뉴 그룹 수정",
        tags = MgmtAdminSwaggerTags.MENU
    )
    @PutMapping("groups/{id}")
    public ResponseEntity<Void> updateMenuGroups(
        @PathVariable Long id,
        @Valid @RequestBody MenuGroupRequest request,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        groupService.update(id, request);
        return ResponseEntity.ok().build();
    }

    // 메뉴 그룹 삭제

    // 목록 조회

    // 단건 조회

    // 등록
    @ApiOperation(
        value = "메뉴 등록",
        tags = MgmtAdminSwaggerTags.MENU
    )
    @PostMapping
    public ResponseEntity<Void> createMenu(@Valid @RequestBody MenuRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 수정
    @ApiOperation(
        value = "메뉴 수정",
        tags = MgmtAdminSwaggerTags.MENU
    )
    @PutMapping("{id}")
    public ResponseEntity<Void> updateMenu(
        @PathVariable Long id,
        @Valid @RequestBody MenuRequest request,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.update(id, request);
        return ResponseEntity.ok().build();
    }

    // 삭제
    @ApiOperation(
        value = "메뉴 삭제",
        tags = MgmtAdminSwaggerTags.MENU
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
