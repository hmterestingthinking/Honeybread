package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceModifyRequest;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(controllers = StoreDeliveryPriceController.class)
@RequiredArgsConstructor
class StoreDeliveryPriceControllerTest {

    final static String BASE_URL = "/stores/delivery-prices";

    final MockMvc mockMvc;

    final ObjectMapper objectMapper;

    @MockBean
    StoreDeliveryPriceService service;

    @Test
    void 주소별_배달금액_생성() throws Exception {
        //given
        final StoreDeliveryPriceRequest request = 주소별_배달금액_요청_성공();

        //when
        final ResultActions resultActions = 주소별_배달금액_생성(request);

        //then
        주소별_배달금액이_생성되어야함();
        결과응답이_예상과_같아야함(HttpStatus.CREATED, resultActions);
    }

    @Test
    void 주소별_배달금액_생성_실패() throws Exception {
        //given
        final StoreDeliveryPriceRequest request = 주소별_배달금액_요청_실패();

        //when
        final ResultActions resultActions = 주소별_배달금액_생성(request);

        //then
        주소별_배달금액이_생성되지_않아야함();
        결과응답이_예상과_같아야함(HttpStatus.BAD_REQUEST, resultActions);
        주소별_배달금액_생성_실패_에러_검증(resultActions);
    }

    @Test
    void 주소별_배달금액_수정() throws Exception {
        //given
        final Long storeId = 1L;
        final StoreDeliveryPriceModifyRequest request = 주소별_배달금액_수정_요청_성공();

        //when
        final ResultActions resultActions = 주소별_배달금액_수정(request, storeId);

        //then
        주소별_배달금액이_수정되어야함();
        결과응답이_예상과_같아야함(HttpStatus.OK, resultActions);
    }

    @Test
    void 주소별_배달금액_수정_실패() throws Exception {
        //given
        final Long storeId = 1L;
        final StoreDeliveryPriceModifyRequest request = 주소별_배달금액_수정_요청_실패();

        //when
        final ResultActions resultActions = 주소별_배달금액_수정(request, storeId);

        //then
        주소별_배달금액이_수정되지않아야함();
        결과응답이_예상과_같아야함(HttpStatus.BAD_REQUEST, resultActions);
        주소별_배달금액_수정_실패_에러_검증(resultActions);
    }

    private void 주소별_배달금액_수정_실패_에러_검증(final ResultActions resultActions) throws Exception {
        resultActions.andExpect(jsonPath("$.errors", hasSize(1)));
    }

    private void 주소별_배달금액이_수정되어야함() {
        then(service).should().update(anyLong(), any(StoreDeliveryPriceModifyRequest.class));
    }
    private void 주소별_배달금액이_수정되지않아야함() {
        then(service).should(never()).update(anyLong(), any(StoreDeliveryPriceModifyRequest.class));
    }

    private ResultActions 주소별_배달금액_수정(final StoreDeliveryPriceModifyRequest request, final Long storeId) throws Exception {
        return mockMvc.perform(put(BASE_URL + "/" + storeId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andDo(print());
    }

    private StoreDeliveryPriceModifyRequest 주소별_배달금액_수정_요청_성공() {
        return new StoreDeliveryPriceModifyRequest(Money.wons(1000));
    }

    private StoreDeliveryPriceModifyRequest 주소별_배달금액_수정_요청_실패() {
        return new StoreDeliveryPriceModifyRequest(null);
    }

    private void 주소별_배달금액_생성_실패_에러_검증(final ResultActions resultActions) throws Exception {
        resultActions.andExpect(jsonPath("$.errors", hasSize(2)));
    }

    private ResultActions 주소별_배달금액_생성(final StoreDeliveryPriceRequest request) throws Exception {
        return mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andDo(print());
    }

    private void 주소별_배달금액이_생성되지_않아야함() {
        then(service).should(never()).create(any(StoreDeliveryPriceRequest.class));
    }

    private void 결과응답이_예상과_같아야함(final HttpStatus expect, final ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is(expect.value()));
    }

    private void 주소별_배달금액이_생성되어야함() {
        then(service).should().create(any(StoreDeliveryPriceRequest.class));
    }

    private StoreDeliveryPriceRequest 주소별_배달금액_요청_성공() {
        return new StoreDeliveryPriceRequest(1L, "서울시 강남구 수서동", Money.wons(1000));
    }

    private StoreDeliveryPriceRequest 주소별_배달금액_요청_실패() {
        return new StoreDeliveryPriceRequest(null, "", Money.wons(1000));
    }
}