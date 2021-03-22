package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.mgmtadmin.support.MgmtAdminSwaggerTags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RestController
@RequestMapping("stores/favorites")
@RequiredArgsConstructor
public class UserStoreFavoriteController {

    private final UserStoreFavoriteService userStoreFavoriteService;
    private final ChannelUserSession session;

    @ApiOperation(
            value = "유저-스토어 찜 등록",
            tags = MgmtAdminSwaggerTags.USER_STORE_FAVORITE
    )
    @PostMapping("{storeId}")
    public ResponseEntity createUserStoreFavorite(@PathVariable Long storeId) {
        userStoreFavoriteService.create(session.getId(), storeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
