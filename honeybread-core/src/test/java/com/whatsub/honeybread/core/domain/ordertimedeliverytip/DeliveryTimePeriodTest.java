package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryTimePeriodTest {

    @Test
    void 시간별_배달팁_생성시_자정_이하시간은_음수로_저장되는지_확인() {
        //given
        final DeliveryTimePeriod deliveryTimePeriod = DeliveryTimePeriod.builder()
            .from(LocalTime.of(23, 0))
            .to(LocalTime.of(5, 0))
            .build();

        //when
        final Integer fromMinuteAtMidnight = deliveryTimePeriod.getFromMinuteByMidnight();
        final Integer toMinuteAtMidnight = deliveryTimePeriod.getToMinuteByMidnight();

        //then
        assertEquals(-60, fromMinuteAtMidnight);
        assertEquals(300, toMinuteAtMidnight);
    }

}