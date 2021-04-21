package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = OrderPriceDeliveryTipService.class)
class OrderPriceDeliveryTipServiceTest {

    final OrderPriceDeliveryTipService service;

    @MockBean
    OrderPriceDeliveryTipRepository repository;

    @Mock
    OrderPriceDeliveryTip orderPriceDeliveryTip;

    @Test
    void 주문가격별_배달팁_생성() {
        //given
        final OrderPriceDeliveryTipRequest request = 주문가격별_배달팁_요청_생성();
        주문가격별_배달팁이_중복되지_않음();
        //when
        service.create(request);
        
        //then
        주문가격별_배달팁이_생성되어야함();
        주문가격별_배달팁이_중복되는지_확인되어야함();
    }

    @Test
    void 주문가격별_배달팁_생성_실패() {
        //given
        final OrderPriceDeliveryTipRequest request = 주문가격별_배달팁_요청_생성();
        주문가격별_배달팁이_중복됨();

        //when
        final HoneyBreadException actual = assertThrows(HoneyBreadException.class, () -> service.create(request));

        //then
        주문가격별_배달팁이_생성되지않아야함();
        주문가격별_배달팁이_중복되는지_확인되어야함();
        반환된_에러가_예상과_같은지확인(ErrorCode.DUPLICATE_ORDER_PRICE_DELIVERY_TIP, actual);
    }

    private void 반환된_에러가_예상과_같은지확인(final ErrorCode duplicateOrderPriceDeliveryTip, final HoneyBreadException actual) {
        assertEquals(duplicateOrderPriceDeliveryTip, actual.getErrorCode());
    }

    private void 주문가격별_배달팁이_중복되지_않음() {
        given(repository.existsByStoreIdAndFromPriceAndToPrice(anyLong(), any(Money.class), any(Money.class)))
                .willReturn(false);
    }

    private void 주문가격별_배달팁이_중복됨() {
        given(repository.existsByStoreIdAndFromPriceAndToPrice(anyLong(), any(Money.class), any(Money.class)))
                .willReturn(true);
    }

    private void 주문가격별_배달팁이_중복되는지_확인되어야함() {
        then(repository).should().existsByStoreIdAndFromPriceAndToPrice(anyLong(), any(Money.class), any(Money.class));
    }

    private void 주문가격별_배달팁이_생성되어야함() {
        then(repository).should().save(any(OrderPriceDeliveryTip.class));
    }

    private void 주문가격별_배달팁이_생성되지않아야함() {
        then(repository).should(never()).save(any(OrderPriceDeliveryTip.class));
    }

    private OrderPriceDeliveryTipRequest 주문가격별_배달팁_요청_생성(){
        return new OrderPriceDeliveryTipRequest(1L, Money.wons(10000), Money.wons(15000), Money.wons(1000));
    }

}