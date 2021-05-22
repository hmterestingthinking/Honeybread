package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementSearch;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementResponse;
import com.whatsub.honeybread.mgmtadmin.support.MgmtAdminSwaggerTags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RestController
@RequestMapping("advertisements")
@RequiredArgsConstructor
public class AdvertisementController {
    private final AdvertisementQueryService queryService;

    @ApiOperation(
        value = "광고 목록 조회",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT
    )
    @GetMapping
    public ResponseEntity<Page<AdvertisementResponse>> getAdvertisements(
        @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt")
        final Pageable pageable,
        final AdvertisementSearch search
    ) {
        return ResponseEntity.ok(queryService.getAdvertisements(pageable, search));
    }
}
