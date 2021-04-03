package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementBidNoticeRepository;
import com.whatsub.honeybread.core.domain.advertisement.validator.AdvertiseBidNoticeValidator;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementBidNoticeRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = AdvertisementBidNoticeService.class)
@RequiredArgsConstructor
class AdvertisementBidNoticeServiceTest {

    final AdvertisementBidNoticeService service;

    @MockBean
    AdvertiseBidNoticeValidator validator;

    @MockBean
    AdvertisementBidNoticeRepository repository;

    @Mock
    AdvertisementBidNotice entity;

    // 등록
    @Test
    void 벨리데이션_성공시_등록_성공() {
        // given
        given(entity.getId()).willReturn(1L);
        willDoNothing().given(validator).validate(any(AdvertisementBidNotice.class));
        given(repository.save(any(AdvertisementBidNotice.class))).willReturn(entity);

        // when
        입찰공고_등록();

        // then
        then(validator).should().validate(any(AdvertisementBidNotice.class));
        then(repository).should().save(any(AdvertisementBidNotice.class));
        then(entity).should().getId();
    }

    @Test
    void 벨리데이션_실패시_등록_실패() {
        // given
        willThrow(ValidationException.class).given(validator).validate(any(AdvertisementBidNotice.class));
        given(repository.save(any(AdvertisementBidNotice.class))).willReturn(entity);

        // when
        assertThrows(ValidationException.class, this::입찰공고_등록);

        // then
        then(validator).should().validate(any(AdvertisementBidNotice.class));
        then(repository).should(never()).save(any(AdvertisementBidNotice.class));
        then(entity).should(never()).getId();
    }

    // 수정

    // 삭제

    // 종료 (마감)

    private AdvertisementBidNoticeRequest 공고_요청() {
        final AdvertisementType 광고_타입 = AdvertisementType.OPEN_LIST;
        final TimePeriod 광고_기간 = TimePeriod.of(
            LocalDateTime.of(2021, 4, 3, 12, 0, 0),
            LocalDateTime.of(2021, 5, 3, 12, 0, 0)
        );
        final Money 최소_입찰_금액 = Money.wons(100_000);
        final Money 입찰_단위 = Money.wons(10_000);
        final int 최대_낙찰_가능_스토어_수 = 10;
        return AdvertisementBidNoticeRequest.builder()
            .type(광고_타입)
            .period(광고_기간)
            .minimumBidPrice(최소_입찰_금액)
            .bidPriceUnit(입찰_단위)
            .maximumStoreCounts(최대_낙찰_가능_스토어_수)
            .build();
    }

    private void 입찰공고_등록() {
        service.create(공고_요청());
    }
}