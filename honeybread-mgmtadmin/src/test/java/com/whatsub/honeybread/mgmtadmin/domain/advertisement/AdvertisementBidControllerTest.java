package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementBidNoticeRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(AdvertisementBidController.class)
@RequiredArgsConstructor
class AdvertisementBidControllerTest {
    static final String BASE_URL = "/advertisements/bid";
    static final String NOTICE_BASE_URL = BASE_URL + "/notices";

    final MockMvc mockMvc;
    final ObjectMapper mapper;

    @MockBean
    AdvertisementBidNoticeService service;

    @Test
    void 벨리데이션_성공시_입찰공고_등록에_성공한다() throws Exception {
        // given
        입찰공고_등록에_성공한다();

        // when
        ResultActions result = 입찰공고_등록();

        // then
        입찰공고_등록이_수행되어야_한다();
        result.andExpect(status().isCreated());
    }

    @Test
    void 벨리데이션_실패시_입찰공고_등록에_실패한다() throws Exception {
        // given
        입찰공고_등록시_벨리데이션에_실패한다();

        // when
        ResultActions result = 입찰공고_등록();

        // then
        입찰공고_등록이_수행되어야_한다();
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 잘못된_요청시_입찰공고_등록에_실패한다() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
            post(NOTICE_BASE_URL)
                .content(mapper.writeValueAsString(잘못된_공고_요청()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        입찰공고_등록이_수행되어서는_안된다();
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 벨리데이션_성공시_입찰공고_수정에_성공한다() throws Exception {
        // given
        입찰공고_수정시_성공한다();

        // when
        ResultActions result = 입찰공고_수정();

        // then
        입찰공고_수정이_수행되어야_한다();
        result.andExpect(status().isOk());
    }

    @Test
    void 벨리데이션_실패시_입찰공고_수정에_실패한다() throws Exception {
        // given
        입찰공고_수정시_벨리데이션에_실패한다();

        // when
        ResultActions result = 입찰공고_수정();

        // then
        입찰공고_수정이_수행되어야_한다();
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 잘못된_요청시_입찰공고_수정에_실패한다() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
            put(NOTICE_BASE_URL + "/1")
                .content(mapper.writeValueAsString(잘못된_공고_요청()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        입찰공고_수정이_수행되어서는_안된다();
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void 입찰공고가_존재하지_않는다면_수정에_실패한다() throws Exception {
        // given
        입찰공고_수정시_입찰공고를_찾지_못한다();

        // when
        ResultActions result = 입찰공고_수정();

        // then
        입찰공고_수정이_수행되어야_한다();
        result.andExpect(status().isNotFound());
    }

    @Test
    void 입찰공고가_진행중이라면_수정에_실패한다() throws Exception {
        // given
        입찰공고_수정시_입찰이_진행중이다();

        // when
        ResultActions result = 입찰공고_수정();

        // then
        입찰공고_수정이_수행되어야_한다();
        result.andExpect(status().is4xxClientError());
    }

    /**
     * Given
     */
    private void 입찰공고_등록에_성공한다() {
        given(service.create(any(AdvertisementBidNoticeRequest.class))).willReturn(1L);
    }

    private void 입찰공고_등록시_벨리데이션에_실패한다() {
        willThrow(ValidationException.class).given(service).create(any(AdvertisementBidNoticeRequest.class));
    }

    private void 입찰공고_수정시_성공한다() {
        willDoNothing().given(service).update(anyLong(), any(AdvertisementBidNoticeRequest.class));
    }

    private void 입찰공고_수정시_벨리데이션에_실패한다() {
        willThrow(ValidationException.class).given(service).update(anyLong(), any(AdvertisementBidNoticeRequest.class));
    }

    private void 입찰공고_수정시_입찰공고를_찾지_못한다() {
        willThrow(new HoneyBreadException(ErrorCode.ADVERTISEMENT_BID_NOTICE_NOT_FOUND))
            .given(service).update(anyLong(), any(AdvertisementBidNoticeRequest.class));
    }

    private void 입찰공고_수정시_입찰이_진행중이다() {
        willThrow(new HoneyBreadException(ErrorCode.ADVERTISEMENT_BID_NOTICE_CANNOT_MODIFY))
            .given(service).update(anyLong(), any(AdvertisementBidNoticeRequest.class));
    }

    /**
     * When
     */
    private ResultActions 입찰공고_등록() throws Exception {
        return mockMvc.perform(
            post(NOTICE_BASE_URL)
                .content(mapper.writeValueAsString(공고_요청()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    private ResultActions 입찰공고_수정() throws Exception {
        return mockMvc.perform(
            put(NOTICE_BASE_URL + "/1")
                .content(mapper.writeValueAsString(공고_요청()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    /**
     * Then
     */
    private void 입찰공고_등록이_수행되어야_한다() {
        then(service).should().create(any(AdvertisementBidNoticeRequest.class));
    }

    private void 입찰공고_등록이_수행되어서는_안된다() {
        then(service).should(never()).create(any(AdvertisementBidNoticeRequest.class));
    }

    private void 입찰공고_수정이_수행되어야_한다() {
        then(service).should().update(anyLong(), any(AdvertisementBidNoticeRequest.class));
    }

    private void 입찰공고_수정이_수행되어서는_안된다() {
        then(service).should(never()).update(anyLong(), any(AdvertisementBidNoticeRequest.class));
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
        final int 최대_낙찰_가능_스토어_수 = 50;
        return AdvertisementBidNoticeRequest.builder()
            .type(광고_타입)
            .period(광고_기간)
            .minimumBidPrice(최소_입찰_금액)
            .bidPriceUnit(입찰_단위)
            .maximumStoreCounts(최대_낙찰_가능_스토어_수)
            .build();
    }

    private AdvertisementBidNoticeRequest 잘못된_공고_요청() {
        final AdvertisementType 광고_타입 = AdvertisementType.OPEN_LIST;
        final TimePeriod 광고_기간 = TimePeriod.of(
            LocalDateTime.of(2021, 4, 3, 12, 0, 0),
            LocalDateTime.of(2021, 4, 3, 12, 0, 0)
        );
        final Money 최소_입찰_금액 = Money.wons(1000);
        final Money 입찰_단위 = Money.wons(500);
        final int 최대_낙찰_가능_스토어_수 = 3;
        return AdvertisementBidNoticeRequest.builder()
            .type(광고_타입)
            .period(광고_기간)
            .minimumBidPrice(최소_입찰_금액)
            .bidPriceUnit(입찰_단위)
            .maximumStoreCounts(최대_낙찰_가능_스토어_수)
            .build();
    }
}