package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.google.common.collect.ImmutableMap;
import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementResponse;
import com.whatsub.honeybread.mgmtadmin.support.MgmtAdminSwaggerTags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RestController
@RequestMapping("advertisements")
@RequiredArgsConstructor
public class AdvertisementController {
    private final AdvertisementQueryService queryService;

    // 광고 목록 조회
    // 광고 기간 / 타입 / 광고 대상 스토어 수
    @ApiOperation(
        value = "광고 목록 조회",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT
    )
    @GetMapping
    public Page<AdvertisementResponse> getAdvertisements() {
        return null;
    }

    // 광고 상세 조회
}
