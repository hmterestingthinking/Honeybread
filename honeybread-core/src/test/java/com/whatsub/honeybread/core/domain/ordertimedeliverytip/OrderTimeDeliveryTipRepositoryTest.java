package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.time.LocalTime;

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
        final OrderTimeDeliveryTip orderTimeDeliveryTip = OrderTimeDeliveryTip.builder()
            .storeId(anyLong())
            .tip(Money.wons(anyLong()))
            .deliveryTimePeriod(DeliveryTimePeriod.builder()
                .from(LocalTime.of(anyInt(), anyInt()))
                .to(LocalTime.of(anyInt(), anyInt()))
                .build())
            .build();

        repository.save(orderTimeDeliveryTip);

        //when
        final boolean result = repository.existsByStoreId(anyLong());

        //then
        assertTrue(result);
    }

}