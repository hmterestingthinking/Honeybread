package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTip;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTipRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = OrderTimeDeliveryTipService.class)
class OrderTimeDeliveryTipServiceTest {

    final OrderTimeDeliveryTipService service;

    @MockBean
    OrderTimeDeliveryTipRepository repository;

    @Test
    void 시간별_배달팁_생성() {
        //given
        final long storeId = 1L;
        final OrderTimeDeliveryTipRequest request =
            시간별_배달팁_요청_생성(LocalTime.of(23,00)
                , LocalTime.of( 5, 0)
                , 1000
                , List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

        //when
        service.create(storeId, request);

        //then
        시간별_배달팁이_생성되어야함();
        시간별_배달팁_중복체크가_되어야함();
    }

    @Test
    void 시간별_배달팁_생성시_storeId_중복_생성_실패() {
        //given
        final long storeId = 1L;
        final OrderTimeDeliveryTipRequest request =
            시간별_배달팁_요청_생성(LocalTime.of(23,00)
                , LocalTime.of( 5, 0)
                , 1000
                , List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

        시간별_배달팁이_중복됨();

        //when
        final HoneyBreadException actual
            = assertThrows(HoneyBreadException.class, () -> service.create(storeId, request));

        //then
        시간별_배달팁이_생성되지_않아야함();
        시간별_배달팁_중복체크가_되어야함();
        반환된_에러가_예상과_같은지확인(ErrorCode.DUPLICATE_ORDER_TIME_DELIVERY_TIP, actual);
    }

    @Test
    void 시간별_배달팁_삭제() {
        //given
        final long storeId = 1L;
        시간별_배달팁이_storeId로_검색();

        //when
        service.remove(storeId);

        //then
        시간별_배달팁이_삭제되어야함();
        시간별_배달팁이_storeId로_검색되어야함();
    }

    @Test
    void 시간별_배달팁_삭제_실패() {
        //given
        final long storeId = 1L;

        //when
        final HoneyBreadException actual
            = assertThrows(HoneyBreadException.class, () -> service.remove(storeId));

        //then
        시간별_배달팁이_삭제되지_않아야함();
        시간별_배달팁이_storeId로_검색되어야함();
        반환된_에러가_예상과_같은지확인(ErrorCode.ORDER_TIME_DELIVERY_TIP_NOT_FOUND, actual);
    }

    /**
     * given
     */

    private void 시간별_배달팁이_storeId로_검색() {
        given(repository.findByStoreId(anyLong()))
            .willReturn(Optional.of(mock(OrderTimeDeliveryTip.class)));
    }

    private void 시간별_배달팁이_중복됨() {
        given(repository.existsByStoreId(anyLong()))
            .willReturn(true);
    }

    private OrderTimeDeliveryTipRequest 시간별_배달팁_요청_생성(final LocalTime from,
                                                      final LocalTime to,
                                                      final int price,
                                                      List<DayOfWeek> days) {
        return new OrderTimeDeliveryTipRequest(from, to, Money.wons(price), days, false, false);
    }

    /**
     * then
     */

    private void 시간별_배달팁이_삭제되지_않아야함() {
        then(repository).should(never()).delete(any(OrderTimeDeliveryTip.class));
    }

    private void 시간별_배달팁이_storeId로_검색되어야함() {
        then(repository).should().findByStoreId(anyLong());
    }

    private void 시간별_배달팁이_삭제되어야함() {
        then(repository).should().delete(any(OrderTimeDeliveryTip.class));
    }

    private void 반환된_에러가_예상과_같은지확인(final ErrorCode duplicateOrderPriceDeliveryTip,
                                   final HoneyBreadException actual) {
        assertEquals(duplicateOrderPriceDeliveryTip, actual.getErrorCode());
    }

    private void 시간별_배달팁이_생성되지_않아야함() {
        then(repository).should(never()).save(any(OrderTimeDeliveryTip.class));
    }

    private void 시간별_배달팁_중복체크가_되어야함() {
        then(repository).should().existsByStoreId(anyLong());
    }

    private void 시간별_배달팁이_생성되어야함() {
        then(repository).should().save(any(OrderTimeDeliveryTip.class));
    }

}