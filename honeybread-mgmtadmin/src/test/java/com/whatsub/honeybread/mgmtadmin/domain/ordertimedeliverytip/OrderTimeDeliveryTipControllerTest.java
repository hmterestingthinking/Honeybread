package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@WebMvcTest(controllers = OrderTimeDeliveryTipController.class)
class OrderTimeDeliveryTipControllerTest {

    static final String BASE_URL = "/stores/1/order-time-delivery-tips";

    final MockMvc mockMvc;

    final ObjectMapper objectMapper;

    @MockBean
    OrderTimeDeliveryTipService service;

    @Test
    void 시간별_배달팁_생성() throws Exception {
        //given
        final OrderTimeDeliveryTipRequest request = 시간별_배달팁_생성_요청();

        //when
        final ResultActions resultActions = 시간별_배달팁_생성_요청(request);

        //then
        결과응답이_예상과_같아야함(HttpStatus.CREATED, resultActions);
        시간별_배달팁이_생성되어야함();
    }

    @Test
    void 시간별_배달팁_생성시_유효성검사_실패() throws Exception {
        //given
        final OrderTimeDeliveryTipRequest request = 시간별_배달팁_생성_실패_요청();

        //when
        final ResultActions resultActions = 시간별_배달팁_생성_요청(request);

        //then
        결과응답이_예상과_같아야함(HttpStatus.BAD_REQUEST, resultActions);
        시간별_배달팁이_생성되지_않아야함();
    }

    private OrderTimeDeliveryTipRequest 시간별_배달팁_생성_실패_요청() {
        return new OrderTimeDeliveryTipRequest(null, null, null);
    }

    private void 시간별_배달팁이_생성되지_않아야함() {
        then(service).should(never()).create(anyLong(), any(OrderTimeDeliveryTipRequest.class));
    }

    private void 시간별_배달팁이_생성되어야함() {
        then(service).should().create(anyLong(), any(OrderTimeDeliveryTipRequest.class));
    }

    private ResultActions 시간별_배달팁_생성_요청(final OrderTimeDeliveryTipRequest request) throws Exception {
        return mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andDo(print());
    }

    private OrderTimeDeliveryTipRequest 시간별_배달팁_생성_요청() {
        return new OrderTimeDeliveryTipRequest(LocalTime.of(23, 0),
            LocalTime.of(23, 0),
            Money.wons(1000));
    }

    public void 결과응답이_예상과_같아야함(final HttpStatus expect, final ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is(expect.value()));
    }


}