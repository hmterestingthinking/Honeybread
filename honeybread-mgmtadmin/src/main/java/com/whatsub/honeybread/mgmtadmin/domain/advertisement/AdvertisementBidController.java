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
    private final AdvertisementBidNoticeService service;
    private final AdvertisementBidNoticeQueryService queryService;

    @ApiOperation(
        value = "입찰 공고 목록",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @GetMapping
    public ResponseEntity<Page<AdvertisementBidNoticeResponse>> getBidNotices(
        @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt")
        final Pageable pageable,
        final AdvertisementBidNoticeSearch search
    ) {
        return ResponseEntity.ok(queryService.getAdvertisementBidNotices(pageable, search));
    }

    @ApiOperation(
        value = "입찰공고 조회",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementBidNoticeResponse> getBidNotice(@PathVariable final Long id) {
        return ResponseEntity.ok(queryService.getAdvertisementBidNotice(id));
    }

    @ApiOperation(
        value = "입찰 공고 등록",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @PostMapping
    public ResponseEntity<Void> createBidNotice(
        @Valid @RequestBody final AdvertisementBidNoticeRequest request,
        final BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(
        value = "입찰 공고 수정",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBidNotice(
        @PathVariable final Long id,
        @Valid @RequestBody final AdvertisementBidNoticeRequest request,
        final BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        service.update(id, request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(
        value = "입찰 공고 삭제",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBidNotice(@PathVariable final Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(
        value = "입찰 공고 종료",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> closeBidNotice(@PathVariable final Long id) {
        service.close(id);
        return ResponseEntity.ok().build();
    }
}
