package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipRepository;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipValidator;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

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

    @MockBean
    OrderPriceDeliveryTipValidator validator;

    @Mock
    OrderPriceDeliveryTip mockEntity;

    final Long storeId = 1L;

    @Test
    void 주문가격별_배달팁_생성() {
        //given
        final OrderPriceDeliveryTipRequest request = 주문가격별_배달팁_요청_생성();
        주문가격별_배달팁이_중복되지_않음();
        //when

        service.create(storeId, request);
        
        //then
        주문가격별_배달팁이_생성되어야함();
        주문가격별_배달팁이_중복되는지_확인되어야함();
        주문가격별_배달팁_유효성검사가_실행되어야함();
    }

    private void 주문가격별_배달팁_유효성검사가_실행되어야함() {
        then(validator).should().validate(any(OrderPriceDeliveryTip.class));
    }

    @Test
    void 주문가격별_배달팁_생성_실패() {
        //given
        final OrderPriceDeliveryTipRequest request = 주문가격별_배달팁_요청_생성();
        주문가격별_배달팁이_중복됨();

        //when
        final HoneyBreadException actual = assertThrows(HoneyBreadException.class, () -> service.create(storeId, request));

        //then
        주문가격별_배달팁이_생성되지않아야함();
        주문가격별_배달팁이_중복되는지_확인되어야함();
        반환된_에러가_예상과_같은지확인(ErrorCode.DUPLICATE_ORDER_PRICE_DELIVERY_TIP, actual);
    }

    @Test
    void 주문가격별_배달팁_삭제() {
        //given
        주문가격별_배달팁이_Id로_검색됨();

        //when
        service.delete(anyLong());

        //then
        주문가격별_배달팁이_삭제되어야함();
        주문가격별_배달팁이_Id로_검색되어야함();
    }

    @Test
    void 주문가격별_배달팁_삭제_실패() {
        //given
        주문가격별_배달팁이_Id로_검색되지_않음();

        //when
        final HoneyBreadException actual = assertThrows(HoneyBreadException.class, () -> service.delete(anyLong()));

        //then
        주문가격별_배달팁이_삭제되지_않아야함();
        주문가격별_배달팁이_Id로_검색되어야함();
        반환된_에러가_예상과_같은지확인(ErrorCode.ORDER_PRICE_DELIVERY_TIP_NOT_FOUND, actual);
    }

    /**
     * given
     */

    private OrderPriceDeliveryTipRequest 주문가격별_배달팁_요청_생성(){
        return new OrderPriceDeliveryTipRequest(Money.wons(10000), Money.wons(15000), Money.wons(1000));
    }

    private void 주문가격별_배달팁이_Id로_검색되지_않음() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());
    }

    private void 주문가격별_배달팁이_Id로_검색됨() {
        given(repository.findById(anyLong())).willReturn(Optional.of(mockEntity));
    }

    private void 주문가격별_배달팁이_중복되지_않음() {
        given(repository.existsByStoreIdAndFromPriceAndToPrice(anyLong(), any(Money.class), any(Money.class)))
                .willReturn(false);
    }

    private void 주문가격별_배달팁이_중복됨() {
        given(repository.existsByStoreIdAndFromPriceAndToPrice(anyLong(), any(Money.class), any(Money.class)))
                .willReturn(true);
    }


    /**
     * then
     */

    private void 주문가격별_배달팁이_Id로_검색되어야함() {
        then(repository).should().findById(anyLong());
    }

    private void 주문가격별_배달팁이_삭제되지_않아야함() {
        then(repository).should(never()).delete(any(OrderPriceDeliveryTip.class));
    }

    private void 주문가격별_배달팁이_삭제되어야함() {
        then(repository).should().delete(any(OrderPriceDeliveryTip.class));
    }

    private void 반환된_에러가_예상과_같은지확인(final ErrorCode duplicateOrderPriceDeliveryTip,
                                   final HoneyBreadException actual) {
        assertEquals(duplicateOrderPriceDeliveryTip, actual.getErrorCode());
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

}