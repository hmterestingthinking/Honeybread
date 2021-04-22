package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipRequest;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
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

@WebMvcTest(controllers = OrderPriceDeliveryTipController.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderPriceDeliveryTipControllerTest {

    static final String BASE_URL = "/stores/1/order-price-delivery-tips";

    final MockMvc mockMvc;
    final ObjectMapper objectMapper;

    @MockBean
    OrderPriceDeliveryTipQueryService queryService;

    @MockBean
    OrderPriceDeliveryTipService service;

    @Test
    void 주문금액별_배달비_StoreId로_조회() throws Exception {
        //given
        final int size = 5;
        주문금액별_배달비가_StoreId로_조회됨(size);

        //when
        final ResultActions resultActions = 주문금액별_배달비_전체_조회();

        //then
        결과응답이_예상과_같아야함(HttpStatus.OK, resultActions);
        주문금액별_배달비가_StoreId로_조회되어야함();
        주문금액별_배달비_전체_조회_검증(resultActions, size);
    }

    @Test
    void 주문금액별_배달비_생성() throws Exception {
        //given
        final OrderPriceDeliveryTipRequest request = 주문금액별_배달비_생성_요청();

        //when
        final ResultActions resultActions = 주문금액별_배달비_생성(request);

        //then
        결과응답이_예상과_같아야함(HttpStatus.CREATED, resultActions);
        주문금액별_배달비가_생성되어야함();
    }

    @Test
    void 주문금액별_배달비_생성_유효성검사_실패() throws Exception {
        //given
        final OrderPriceDeliveryTipRequest request = 주문금액별_배달비_생성_요청_실패();

        //when
        final ResultActions resultActions = 주문금액별_배달비_생성(request);

        //then
        결과응답이_예상과_같아야함(HttpStatus.BAD_REQUEST, resultActions);
        주문금액별_배달비가_생성되지않아야함();
        주문금액별_배달비가_생성_유효성검사_검증(resultActions);
    }

    @Test
    void 주문금액별_배달비_삭제() throws Exception {
        //given
        final Long id = anyLong();

        //when
        final ResultActions resultActions = 주문금액별_배달비_삭제(id);

        //then
        결과응답이_예상과_같아야함(HttpStatus.NO_CONTENT, resultActions);
        주문금액별_배달비가_삭제되어야함();
    }

    /**
     * given
     */
    private OrderPriceDeliveryTipRequest 주문금액별_배달비_생성_요청() {
        return new OrderPriceDeliveryTipRequest(Money.wons(10000), Money.wons(Integer.MAX_VALUE), Money.ZERO);
    }

    private OrderPriceDeliveryTipRequest 주문금액별_배달비_생성_요청_실패() {
        return new OrderPriceDeliveryTipRequest(null, null, null);
    }


    /**
     * then
     */
    private void 주문금액별_배달비가_삭제되어야함() {
        then(service).should().delete(anyLong());
    }

    private void 주문금액별_배달비가_생성_유효성검사_검증(final ResultActions resultActions) throws Exception {
        resultActions.andExpect(jsonPath("$.errors", hasSize(2)));
    }

    private void 주문금액별_배달비가_생성되어야함() {
        then(service).should().create(anyLong(), any(OrderPriceDeliveryTipRequest.class));
    }

    private void 주문금액별_배달비가_생성되지않아야함() {
        then(service).should(never()).create(anyLong(), any(OrderPriceDeliveryTipRequest.class));
    }

    public void 결과응답이_예상과_같아야함(final HttpStatus expect, final ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is(expect.value()));
    }

    private void 주문금액별_배달비_전체_조회_검증(final ResultActions resultActions, final int size) throws Exception {
        resultActions.andExpect(jsonPath("$", hasSize(size)));
    }

    private void 주문금액별_배달비가_StoreId로_조회되어야함() {
        then(queryService).should().getAllByStoreId(anyLong());
    }


    /**
     * request
     */

    private ResultActions 주문금액별_배달비_삭제(final Long id) throws Exception {
        return mockMvc.perform(delete(BASE_URL + "/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print());
    }

    private ResultActions 주문금액별_배달비_생성(final OrderPriceDeliveryTipRequest request) throws Exception {
        return mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andDo(print());
    }

    private void 주문금액별_배달비가_StoreId로_조회됨(final int size) {
        final List<OrderPriceDeliveryTipResponse> list = IntStream.range(0, size)
                .mapToObj(ignore -> mock(OrderPriceDeliveryTipResponse.class))
                .collect(Collectors.toList());
        given(queryService.getAllByStoreId(anyLong()))
                .willReturn(list);
    }

    private ResultActions 주문금액별_배달비_전체_조회() throws Exception {
        return mockMvc.perform(get(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

}