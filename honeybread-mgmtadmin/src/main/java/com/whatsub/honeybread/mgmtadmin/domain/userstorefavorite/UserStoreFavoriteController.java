package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.domain.store.dto.StoreResponse;
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
import org.springframework.web.bind.annotation.*;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RestController
@RequestMapping("stores/favorites")
@RequiredArgsConstructor
public class UserStoreFavoriteController {

    private final UserStoreFavoriteService service;
    private final UserStoreFavoriteQueryService queryService;

    private final ChannelUserSession channelUserSession;

    @ApiOperation(
            value = "찜한 스토어 목록 조회",
            tags = MgmtAdminSwaggerTags.USER_STORE_FAVORITE
    )
    @GetMapping
    public ResponseEntity<Page<StoreResponse>> getStores(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable) {
        Page<StoreResponse> response = queryService.getStoresByUserId(pageable, channelUserSession.getId());
        return ResponseEntity.ok(response);
    }

    @ApiOperation(
            value = "유저-스토어 찜 등록",
            tags = MgmtAdminSwaggerTags.USER_STORE_FAVORITE
    )
    @PostMapping("{storeId}")
    public ResponseEntity createUserStoreFavorite(@PathVariable Long storeId) {
        service.create(channelUserSession.getId(), storeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(
            value = "유저-스토어 찜 삭제",
            tags = MgmtAdminSwaggerTags.USER_STORE_FAVORITE
    )
    @DeleteMapping("{storeId}")
    public ResponseEntity deleteUserStoreFavorite(@PathVariable Long storeId) {
        service.delete(channelUserSession.getId(), storeId);
        return ResponseEntity.noContent().build();
    }

}
