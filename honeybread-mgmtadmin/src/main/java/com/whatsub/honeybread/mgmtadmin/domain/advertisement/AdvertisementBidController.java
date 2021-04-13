package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementBidNoticeRequest;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RestController
@RequestMapping("advertisements/bid")
@RequiredArgsConstructor
public class AdvertisementBidController {
    private static final String NOTICE_MAPPING = "notices";

    private final AdvertisementBidNoticeService noticeService;
    private final AdvertisementBidNoticeQueryService noticeQueryService;

    // 상태, 기간검색, 타입 검색필요
    // 입찰 공고 목록
    public ResponseEntity<Page> getBidNotices(
        @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt")
        Pageable pageable
    ) {

        return null;
    }

    // 입찰 공고 상세

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

    @ApiOperation(
        value = "입찰 공고 수정",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @PutMapping(NOTICE_MAPPING + "/{id}")
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

    // 입찰 공고 삭제
    @ApiOperation(
        value = "입찰 공고 삭제",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @DeleteMapping(NOTICE_MAPPING + "/{id}")
    public ResponseEntity<Void> deleteBidNotice(@PathVariable Long id) {
        noticeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(
        value = "입찰 공고 종료",
        tags = MgmtAdminSwaggerTags.ADVERTISEMENT_BID
    )
    @PatchMapping(NOTICE_MAPPING + "/{id}")
    public ResponseEntity<Void> closeBidNotice(@PathVariable Long id) {
        noticeService.close(id);
        return ResponseEntity.ok().build();
    }
}
