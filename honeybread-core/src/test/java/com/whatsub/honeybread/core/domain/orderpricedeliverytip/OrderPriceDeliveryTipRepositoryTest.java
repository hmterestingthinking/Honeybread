package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;

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

    @Test
    void 주문가격별_배달팁을_StoreId로_검색() {
        //given
        final int size = 5;
        final List<OrderPriceDeliveryTip> orderPriceDeliveryTipList = 주문가격별_배달팁을_사이즈만큼_생성(5);

        //when
        repository.saveAll(orderPriceDeliveryTipList);

        //then
        assertEquals(size, repository.findByStoreId(anyLong()).size());
    }

    @ParameterizedTest
    @CsvSource(value = {"10000, 10000, 15000", "10000, 10000, 0"})
    void 최종주문가격에_해당하는_주문가격별_배달팁_검색(final Long orderPrice,
                                   final Long fromPrice,
                                   final Long toPrice) {
        //given
        final long storeId = 1L;
        final OrderPriceDeliveryTip orderPriceDeliveryTip =
            주문가격별_배달팁_생성(storeId, Money.wons(fromPrice), toPrice == 0 ? null : Money.wons(toPrice));
        repository.save(orderPriceDeliveryTip);

        //when
        final Optional<OrderPriceDeliveryTip> findTip = repository.getTipByOrderPrice(storeId, Money.wons(orderPrice));

        //then
        assertTrue(findTip.isPresent());
        assertEquals(orderPriceDeliveryTip, findTip.get());
    }

    @Test
    @DisplayName("특정 금액 이상인(toPrice가 null) 가격 범위가 있는지 확인")
    void 가격범위가_정해지지않은경우가_있는지_확인() {
        //given
        final long storeId = 1L;
        final OrderPriceDeliveryTip orderPriceDeliveryTip = 주문가격별_배달팁_생성(storeId, Money.wons(10000), null);
        repository.save(orderPriceDeliveryTip);

        //when
        final boolean result = repository.existsByStoreIdAndToPriceIsNull(storeId);

        //then
        assertTrue(result);
    }

    private List<OrderPriceDeliveryTip> 주문가격별_배달팁을_사이즈만큼_생성(final int size) {
        return IntStream.range(0, size)
                .mapToObj(ignore -> OrderPriceDeliveryTip.builder()
                        .storeId(anyLong())
                        .toPrice(Money.ZERO)
                        .fromPrice(Money.ZERO)
                        .tip(Money.ZERO)
                        .build())
                .collect(Collectors.toList());
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