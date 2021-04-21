package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@DataJpaTest
class OrderPriceDeliveryTipRepositoryTest {

    final OrderPriceDeliveryTipRepository repository;

    @Test
    void 주문가격별_배달팁이_존재하는지_확인() {
        //given
        final Long storeId = 1L;
        final Money fromPrice = Money.wons(10000);
        final Money toPrice = Money.wons(15000);
        final OrderPriceDeliveryTip orderPriceDeliveryTip = 주문가격별_배달팁_생성(storeId, fromPrice, toPrice);
        repository.save(orderPriceDeliveryTip);

        //when
        final boolean result = repository.existsByStoreIdAndFromPriceAndToPrice(storeId, fromPrice, toPrice);

        //then
        assertTrue(result);
    }

    private OrderPriceDeliveryTip 주문가격별_배달팁_생성(final Long storeId, final Money fromPrice, final Money toPrice) {
        return OrderPriceDeliveryTip.builder()
                .fromPrice(fromPrice)
                .toPrice(toPrice)
                .storeId(storeId)
                .tip(Money.wons(1000))
                .build();
    }

}