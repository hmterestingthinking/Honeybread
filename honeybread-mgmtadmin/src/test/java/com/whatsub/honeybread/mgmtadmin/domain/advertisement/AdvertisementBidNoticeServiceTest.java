package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementBidNoticeRepository;
import com.whatsub.honeybread.core.domain.advertisement.validator.AdvertiseBidNoticeValidator;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementBidNoticeRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

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

    @Test
    void 벨리데이션_성공시_등록_성공() {
        // given
        벨리데이션시_성공한다();
        등록시_성공한다();

        // when
        입찰공고_등록();

        // then
        벨리데이션이_수행되어야_한다();
        입찰공고_등록이_수행되어야_한다();
        입찰공고_식별자가_반환되어야_한다();
    }

    @Test
    void 벨리데이션_실패시_등록_실패() {
        // given
        벨리데이션시_실패한다();
        등록시_성공한다();

        // when
        assertThrows(ValidationException.class, this::입찰공고_등록);

        // then
        벨리데이션이_수행되어야_한다();
        입찰공고_등록이_수행되어서는_안된다();
        입찰공고_식별자가_반환되어서는_안된다();
    }

    @Test
    void 벨리데이션_성공시_수정_성공() {
        // given
        입찰공고_조회시_성공한다();
        입찰이_진행중이_아니다();
        벨리데이션시_성공한다();

        // when
        입찰공고_수정();

        // then
        입찰공고_조회가_수행되어야_한다();
        입찰_진행중_확인이_수행되어야_한다();
        벨리데이션이_수행되어야_한다();
    }

    @Test
    void 벨리데이션_실패시_수정_실패() {
        // given
        입찰공고_조회시_성공한다();
        입찰이_진행중이_아니다();
        벨리데이션시_실패한다();

        // when
        assertThrows(ValidationException.class, this::입찰공고_수정);

        // then
        입찰공고_조회가_수행되어야_한다();
        입찰_진행중_확인이_수행되어야_한다();
        벨리데이션이_수행되어야_한다();
    }

    @Test
    void 입찰공고가_존재하지_않는다면_수정_실패() {
        // given
        입찰공고_조회시_실패한다();

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, this::입찰공고_수정);

        // then
        입찰공고_조회가_수행되어야_한다();
        입찰_진행중_확인이_수행되어서는_안된다();
        벨리데이션이_수행되어서는_안된다();
        입찰공고_찾지못함_에러코드_확인(ex);
    }

    @Test
    void 입찰진행중_이라면_수정_실패() {
        // given
        입찰공고_조회시_성공한다();
        입찰이_진행중이다();
        벨리데이션시_성공한다();

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, this::입찰공고_수정);

        // then
        입찰공고_조회가_수행되어야_한다();
        입찰_진행중_확인이_수행되어야_한다();
        벨리데이션이_수행되어서는_안된다();

        입찰공고_진행_에러코드_확인(ex);
    }

    @Test
    void 입찰공고_삭제_성공() {
        // given
        입찰공고_조회시_성공한다();
        입찰이_진행중이_아니다();

        // when
        입찰공고_삭제();

        // then
        입찰공고_조회가_수행되어야_한다();

    }

    @Test
    void 입찰공고가_존재하지_않는다면_삭제_실패() {
        // given
        입찰공고_조회시_실패한다();

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, this::입찰공고_삭제);

        // then
        입찰공고_조회가_수행되어야_한다();
        입찰공고_찾지못함_에러코드_확인(ex);
    }

    @Test
    void 입찰_진행중이라면_삭제_실패() {
        // given
        입찰공고_조회시_성공한다();
        입찰이_진행중이다();

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, this::입찰공고_삭제);

        // then
        입찰공고_조회가_수행되어야_한다();
        입찰_진행중_확인이_수행되어야_한다();
        입찰공고_진행_에러코드_확인(ex);
    }

    @Test
    void 입찰_종료_성공() {
        // given
        입찰공고_조회시_성공한다();

        // when
        입찰_종료();

        // then
        입찰공고_조회가_수행되어야_한다();
        입찰_종료가_수행되어야_한다();
    }

    @Test
    void 입찰공고가_존재하지_않는다면_종료_실패() {
        // given
        입찰공고_조회시_실패한다();

        // when
        HoneyBreadException ex = assertThrows(HoneyBreadException.class, this::입찰_종료);

        // then
        입찰공고_찾지못함_에러코드_확인(ex);
    }

    /**
     * Given
     */
    private void 등록시_성공한다() {
        given(entity.getId()).willReturn(1L);
        given(repository.save(any(AdvertisementBidNotice.class))).willReturn(entity);
    }

    private void 벨리데이션시_성공한다() {
        willDoNothing().given(validator).validate(any(AdvertisementBidNotice.class));
    }

    private void 벨리데이션시_실패한다() {
        willThrow(ValidationException.class).given(validator).validate(any(AdvertisementBidNotice.class));
    }

    private void 입찰공고_조회시_성공한다() {
        given(repository.findById(anyLong())).willReturn(Optional.of(entity));
    }

    private void 입찰공고_조회시_실패한다() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());
    }

    private void 입찰이_진행중이다() {
        given(entity.isProcess()).willReturn(true);
    }

    private void 입찰이_진행중이_아니다() {
        given(entity.isProcess()).willReturn(false);
    }

    /**
     * When
     */
    private void 입찰공고_등록() {
        service.create(공고_요청());
    }

    private void 입찰공고_수정() {
        service.update(anyLong(), 공고_요청());
    }

    private void 입찰공고_삭제() {
        service.delete(anyLong());
    }

    private void 입찰_종료() {
        service.close(anyLong());
    }

    /**
     * Then
     */
    private void 벨리데이션이_수행되어야_한다() {
        then(validator).should().validate(any(AdvertisementBidNotice.class));
    }

    private void 입찰공고_등록이_수행되어야_한다() {
        then(repository).should().save(any(AdvertisementBidNotice.class));
    }

    private void 입찰공고_등록이_수행되어서는_안된다() {
        then(repository).should(never()).save(any(AdvertisementBidNotice.class));
    }

    private void 입찰공고_식별자가_반환되어야_한다() {
        then(entity).should().getId();
    }

    private void 입찰공고_식별자가_반환되어서는_안된다() {
        then(entity).should(never()).getId();
    }

    private void 입찰공고_조회가_수행되어야_한다() {
        then(repository).should().findById(anyLong());
    }

    private void 입찰_진행중_확인이_수행되어야_한다() {
        then(entity).should().isProcess();
    }

    private void 벨리데이션이_수행되어서는_안된다() {
        then(validator).should(never()).validate(any(AdvertisementBidNotice.class));
    }

    private void 입찰_진행중_확인이_수행되어서는_안된다() {
        then(entity).should(never()).isProcess();
    }

    private void 입찰_종료가_수행되어야_한다() {
        then(entity).should().close();
    }

    private void 입찰공고_진행_에러코드_확인(HoneyBreadException ex) {
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ADVERTISEMENT_BID_NOTICE_CANNOT_MODIFY);
    }

    private void 입찰공고_찾지못함_에러코드_확인(HoneyBreadException ex) {
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ADVERTISEMENT_BID_NOTICE_NOT_FOUND);
    }


    /**
     * Helper
     */
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
}