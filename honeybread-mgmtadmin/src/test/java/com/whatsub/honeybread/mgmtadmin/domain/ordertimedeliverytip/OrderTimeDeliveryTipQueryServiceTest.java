package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTip;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTipRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto.OrderTimeDeliveryTipResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.time.DayOfWeek;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = OrderTimeDeliveryTipQueryService.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class OrderTimeDeliveryTipQueryServiceTest {

    final OrderTimeDeliveryTipQueryService queryService;

    @MockBean
    OrderTimeDeliveryTipRepository repository;

    @Test
    void 시간별_배달팁_검색() {
        //given
        int price = 1000;
        시간별_배달팁_검색_성공(price);

        //when
        final OrderTimeDeliveryTipResponse response = queryService.getTipByTime(anyLong(), any(), any(DayOfWeek.class));

        //then
        assertEquals(price, response.getTip().getValue().intValue());
    }

    @Test
    void 시간별_배달팁_검색시_추가금_없음() {
        //given
        시간별_배달팁_검색_결과_없음();

        //when
        final OrderTimeDeliveryTipResponse response = queryService.getTipByTime(anyLong(), any(), any(DayOfWeek.class));

        //then
        assertEquals(0, response.getTip().getValue().intValue());
    }

    @Test
    void 시간별_배달팁_storeId로_검색() {
        //given
        final long storeId = 1L;
        시간별_배달팁_검색_성공(storeId);

        //when
        final OrderTimeDeliveryTipResponse response = queryService.getTipByStoreId(storeId);

        //then
        assertEquals(storeId, response.getStoreId());
    }

    @Test
    void 시간별_배달팁_storeId로_검색_실패() {
        //given
        final long storeId = 1L;

        //when
        final HoneyBreadException exception
            = assertThrows(HoneyBreadException.class, () -> queryService.getTipByStoreId(storeId));

        //then
        반환된_에러가_예상과_같은지확인(ErrorCode.ORDER_TIME_DELIVERY_TIP_NOT_FOUND, exception);
    }

    /**
     * given
     */

    private void 시간별_배달팁_검색_결과_없음() {
        given(repository.getTipByTime(anyLong(), any(), any(DayOfWeek.class)))
            .willReturn(Optional.empty());
    }

    private void 시간별_배달팁_검색_성공(final long storeId) {
        given(repository.findByStoreId(anyLong()))
            .willReturn(Optional.of(OrderTimeDeliveryTip.builder().storeId(storeId).build()));
    }

    private void 시간별_배달팁_검색_성공(final int price) {
        final OrderTimeDeliveryTip tip = OrderTimeDeliveryTip.builder()
            .tip(Money.wons(price))
            .build();
        given(repository.getTipByTime(anyLong(), any(), any()))
            .willReturn(Optional.of(tip));
    }

    /**
     * then
     */

    private void 반환된_에러가_예상과_같은지확인(final ErrorCode duplicateOrderPriceDeliveryTip,
                                   final HoneyBreadException actual) {
        assertEquals(duplicateOrderPriceDeliveryTip, actual.getErrorCode());
    }

}