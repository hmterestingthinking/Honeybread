package com.whatsub.honeybread.mgmtadmin.domain.store;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.domain.store.dto.StoreSearch;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreCreateRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreResponse;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreUpdateRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RestController
@RequestMapping("stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final StoreQueryService storeQueryService;

    @ApiOperation(
            value = "스토어 목록 조회",
            tags = MgmtAdminSwaggerTags.STORE
    )
    @GetMapping
    public ResponseEntity<Page<StoreResponse>> getStores(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable,
            StoreSearch search
    ) {
        Page<StoreResponse> response = storeQueryService.getStores(pageable, search);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(
            value = "스토어 조회",
            tags = MgmtAdminSwaggerTags.STORE
    )
    @GetMapping("{id}")
    public ResponseEntity<StoreResponse> getStore(@PathVariable Long id) {
        StoreResponse response = storeQueryService.getStoreByStoreId(id);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(
            value = "스토어 등록",
            tags = MgmtAdminSwaggerTags.STORE
    )
    @PostMapping
    public ResponseEntity<Void> createStore(@Valid @RequestBody StoreCreateRequest request,
                                            BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        storeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(
            value = "스토어 수정",
            tags = MgmtAdminSwaggerTags.STORE
    )
    @PutMapping("{storeId}")
    public ResponseEntity<Void> updateStore(@PathVariable long storeId,
                                            @Valid @RequestBody StoreUpdateRequest request,
                                            BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        storeService.update(storeId, request);
        return ResponseEntity.ok().build();
    }

}
