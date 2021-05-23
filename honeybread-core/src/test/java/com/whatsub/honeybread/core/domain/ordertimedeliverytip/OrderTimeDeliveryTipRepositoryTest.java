package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
@RequiredArgsConstructor
class OrderTimeDeliveryTipRepositoryTest {

    final OrderTimeDeliveryTipRepository repository;

    @Test
    void 시간별_배달팁_storeId_등록여부_체크() {
        //given
        final OrderTimeDeliveryTip orderTimeDeliveryTip = 시간별_배달팁_생성();

        repository.save(orderTimeDeliveryTip);

        //when
        final boolean result = repository.existsByStoreId(anyLong());

        //then
        assertTrue(result);
    }

    @Test
    void 시간별_배달팁_storeId로_검색() {
        //given
        final long storeId = anyLong();
        final OrderTimeDeliveryTip orderTimeDeliveryTip = 시간별_배달팁_생성();

        repository.save(orderTimeDeliveryTip);

        //when
        final OrderTimeDeliveryTip findTip = repository.findByStoreId(storeId).get();

        //then
        assertEquals(orderTimeDeliveryTip, findTip);
    }

    @Test
    void 시간별_배달팁_storeId_time_으로_검색() {
        //given
        final long storeId = 1L;
        final OrderTimeDeliveryTip orderTimeDeliveryTip = OrderTimeDeliveryTip.builder()
            .storeId(storeId)
            .tip(Money.wons(anyLong()))
            .deliveryTimePeriod(DeliveryTimePeriod.builder()
                .from(LocalTime.of(23, 0))
                .to(LocalTime.of(8, 0))
                .days(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY))
                .isAllTheTime(false)
                .isAllDay(false)
                .build())
            .build();

        repository.save(orderTimeDeliveryTip);

        //when
        final OrderTimeDeliveryTip findTip
            = repository.getTipByTime(storeId, LocalTime.of(3, 57), DayOfWeek.MONDAY).get();

        //then
        assertEquals(orderTimeDeliveryTip, findTip);
    }

    @Test
    @DisplayName("시간별 배달팁 검색시 isAllTheTime이 true일 경우 검색 시간대와 상관없이 검색 성공")
    void 시간별_배달팁_storeId_time_으로_검색2() {
        //given
        final long storeId = 1L;
        final OrderTimeDeliveryTip orderTimeDeliveryTip = OrderTimeDeliveryTip.builder()
            .storeId(storeId)
            .tip(Money.wons(anyLong()))
            .deliveryTimePeriod(DeliveryTimePeriod.builder()
                .from(LocalTime.of(anyInt(), anyInt()))
                .to(LocalTime.of(anyInt(), anyInt()))
                .days(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY))
                .isAllTheTime(true)
                .isAllDay(false)
                .build())
            .build();

        repository.save(orderTimeDeliveryTip);

        //when
        final OrderTimeDeliveryTip findTip
            = repository.getTipByTime(storeId, LocalTime.of(anyInt(), anyInt()), DayOfWeek.MONDAY).get();

        //then
        assertEquals(orderTimeDeliveryTip, findTip);
    }

    private OrderTimeDeliveryTip 시간별_배달팁_생성() {
        return OrderTimeDeliveryTip.builder()
            .storeId(anyLong())
            .tip(Money.wons(anyLong()))
            .deliveryTimePeriod(DeliveryTimePeriod.builder()
                .from(LocalTime.of(anyInt(), anyInt()))
                .to(LocalTime.of(anyInt(), anyInt()))
                .days(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY))
                .isAllTheTime(false)
                .isAllDay(false)
                .build())
            .build();
    }
}