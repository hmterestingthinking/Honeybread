package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        given(service.create(any(AdvertisementBidNoticeRequest.class))).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(
            post(NOTICE_BASE_URL)
                .content(mapper.writeValueAsString(공고_요청()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result.andExpect(status().isCreated());
    }

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