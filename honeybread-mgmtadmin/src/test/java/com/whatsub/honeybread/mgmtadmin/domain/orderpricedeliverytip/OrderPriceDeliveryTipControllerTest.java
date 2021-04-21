package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderPriceDeliveryTipController.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderPriceDeliveryTipControllerTest {

    static final String BASE_URL = "/stores/1/order-price-delivery-tips";

    final MockMvc mockMvc;

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

    private void 주문금액별_배달비가_StoreId로_조회됨(final int size) {
        final List<OrderPriceDeliveryTipResponse> list = IntStream.range(0, size)
                .mapToObj(ignore -> mock(OrderPriceDeliveryTipResponse.class))
                .collect(Collectors.toList());
        given(queryService.getAllByStoreId(anyLong()))
                .willReturn(list);
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

    private ResultActions 주문금액별_배달비_전체_조회() throws Exception {
        return mockMvc.perform(get(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }


}