package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipRequest;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @MockBean
    OrderTimeDeliveryTipQueryService queryService;

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

    @Test
    void 시간별_배달팁_삭제() throws Exception {
        //given
        final long id = 1;

        //when
        final ResultActions resultActions = 시간별_배달팁_삭제_요청(id);

        //then
        결과응답이_예상과_같아야함(HttpStatus.NO_CONTENT, resultActions);
        시간별_배달팁이_삭제되어야함();
    }

    @Test
    void 시간별_배달팁_StoreId_조회() throws Exception {
        //given
        시간별_배달팁이_StoreId로_조회_성공();

        //when
        final ResultActions resultActions = 시간별_배달팁_StoreId_조회_요청();

        //then
        시간별_배달팁_조회_검증(resultActions);
        결과응답이_예상과_같아야함(HttpStatus.OK, resultActions);

    }

    @Test
    void 시간별_배달팁_StoreId_조회_실패() throws Exception {
        //given
        시간별_배달팁이_StoreId로_조회_실패();

        //when
        final ResultActions resultActions = 시간별_배달팁_StoreId_조회_요청();

        //then
        결과응답이_예상과_같아야함(HttpStatus.NOT_FOUND, resultActions);
    }

    @Test
    void 시간별_배달팁_StoreId_Time_조회() throws Exception {
        //given
        시간별_배달팁이_StoreId_Time으로_조회_성공();

        //when
        final ResultActions resultActions = 시간별_배달팁_StoreId_Time_조회_요청();

        //then
        시간별_배달팁_조회_검증(resultActions);
        결과응답이_예상과_같아야함(HttpStatus.OK, resultActions);
    }

    /**
     * given
     */

    private void 시간별_배달팁이_StoreId_Time으로_조회_성공() {
        given(queryService.getTipByTime(anyLong(), any(LocalTime.class), any(DayOfWeek.class)))
            .willReturn(mock(OrderTimeDeliveryTipResponse.class));
    }

    private void 시간별_배달팁이_StoreId로_조회_실패() {
        given(queryService.getTipByStoreId(anyLong()))
            .willThrow(new HoneyBreadException(ErrorCode.ORDER_TIME_DELIVERY_TIP_NOT_FOUND));
    }

    private void 시간별_배달팁이_StoreId로_조회_성공() {
        given(queryService.getTipByStoreId(anyLong()))
            .willReturn(mock(OrderTimeDeliveryTipResponse.class));
    }

    private OrderTimeDeliveryTipRequest 시간별_배달팁_생성_실패_요청() {
        return new OrderTimeDeliveryTipRequest(null, null, null, null, false, false);
    }

    private OrderTimeDeliveryTipRequest 시간별_배달팁_생성_요청() {
        return new OrderTimeDeliveryTipRequest(LocalTime.of(23, 0),
            LocalTime.of(23, 0),
            Money.wons(1000),
            List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
            false,
            false);
    }

    /**
     * then
     */

    public void 결과응답이_예상과_같아야함(final HttpStatus expect, final ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is(expect.value()));
    }

    private void 시간별_배달팁이_생성되지_않아야함() {
        then(service).should(never()).create(anyLong(), any(OrderTimeDeliveryTipRequest.class));
    }

    private void 시간별_배달팁이_생성되어야함() {
        then(service).should().create(anyLong(), any(OrderTimeDeliveryTipRequest.class));
    }

    private void 시간별_배달팁_조회_검증(final ResultActions resultActions) throws Exception {
        resultActions.andExpect(jsonPath("$").exists());
    }

    private void 시간별_배달팁이_삭제되어야함() {
        then(service).should().remove(anyLong());
    }

    /**
     * request
     */

    private ResultActions 시간별_배달팁_생성_요청(final OrderTimeDeliveryTipRequest request) throws Exception {
        return mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andDo(print());
    }

    private ResultActions 시간별_배달팁_삭제_요청(final long id) throws Exception {
        return mockMvc.perform(delete(BASE_URL + "/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    private ResultActions 시간별_배달팁_StoreId_조회_요청() throws Exception {
        return mockMvc.perform(get(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    private ResultActions 시간별_배달팁_StoreId_Time_조회_요청() throws Exception {
        final LocalTime time = LocalTime.of(0, 0);
        final DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        return mockMvc.perform(get(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .param("time", time.toString())
            .param("dayOfWeek", dayOfWeek.toString())
        ).andDo(print());
    }
}