package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementBidNoticeSearch;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementBidNoticeRequest;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementBidNoticeResponse;
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
@RequestMapping("advertisements/bid-notices")
@RequiredArgsConstructor
public class AdvertisementBidController {
    private final AdvertisementBidNoticeService noticeService;
    private final AdvertisementBidNoticeQueryService noticeQueryService;

    @ApiOperation(
        value = "입찰 공고 목록",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @GetMapping
    public ResponseEntity<Page<AdvertisementBidNoticeResponse>> getBidNotices(
        @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt")
        Pageable pageable,
        AdvertisementBidNoticeSearch search
    ) {
        Page<AdvertisementBidNoticeResponse> response = noticeQueryService.getAdvertisementBidNotices(pageable, search);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(
        value = "입찰공고 조회",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementBidNoticeResponse> getBidNotice(@PathVariable Long id) {
        AdvertisementBidNoticeResponse response = noticeQueryService.getAdvertisementBidNotice(id);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(
        value = "입찰 공고 등록",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @PostMapping
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

    @ApiOperation(
        value = "입찰 공고 수정",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBidNotice(
        @PathVariable Long id,
        @Valid @RequestBody AdvertisementBidNoticeRequest request,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        noticeService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(
        value = "입찰 공고 삭제",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBidNotice(@PathVariable Long id) {
        noticeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(
        value = "입찰 공고 종료",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> closeBidNotice(@PathVariable Long id) {
        noticeService.close(id);
        return ResponseEntity.ok().build();
    }
}
