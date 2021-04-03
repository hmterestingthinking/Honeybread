package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementBidNoticeRequest;
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
@RequestMapping("advertisements/bid")
@RequiredArgsConstructor
public class AdvertisementBidController {
    private static final String NOTICE_MAPPING = "notices";

    private final AdvertisementBidNoticeService noticeService;

    // 입찰 공고 목록

    // 입찰 공고 상세

    // 입찰 공고 등록
    @ApiOperation(
        value = "입찰 공고 등록",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @PostMapping(NOTICE_MAPPING)
    public ResponseEntity<Void> createBidNotice(
        @Valid @RequestBody AdvertisementBidNoticeRequest request,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        noticeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // 입찰 공고 수정

    // 입찰 공고 삭제
}
