package com.whatsub.honeybread.core.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

@DataJpaTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class StoreDeliveryPriceRepositoryTest {

    final StoreDeliveryPriceRepository repository;

    @Test
    void StoreId와_검색가능주소_중복_확인() {
        //given
        final StoreDeliveryPrice addressPrice = 주소별_배달금액_생성(1L, "서울시 강남구 수서동", Money.wons(5000));
        repository.save(addressPrice);

        //when
        final boolean result =
            repository.existsByStoreIdAndSearchableDeliveryAddress(addressPrice.getStoreId(), addressPrice.getSearchableDeliveryAddress());

        //then
        assertTrue(result);
    }

    @Test
    void StoreId로_주소별_배달금액_검색() {
        //given
        final long storeId = 1L;
        List<StoreDeliveryPrice> addressPrice = 주소별_배달금액_사이즈만큼_생성(10, storeId);
        repository.saveAll(addressPrice);

        //when
        final Map<Money, List<StoreDeliveryPrice>> storeDeliveryPrices = repository.getStoreDeliveryPrices(storeId);

        //then
        assertEquals(3, storeDeliveryPrices.size());
        assertEquals(4, storeDeliveryPrices.get(Money.wons(1000)).size());
        assertEquals(3, storeDeliveryPrices.get(Money.wons(2000)).size());
        assertEquals(3, storeDeliveryPrices.get(Money.wons(3000)).size());
    }

    private StoreDeliveryPrice 주소별_배달금액_생성(final Long storeId, final String address, final Money price) {
        return StoreDeliveryPrice.builder()
            .storeId(storeId)
            .searchableDeliveryAddress(address)
            .price(price)
            .build();
    }

    private List<StoreDeliveryPrice> 주소별_배달금액_사이즈만큼_생성(final int size, final Long storeId) {
        return IntStream.range(0, size)
            .mapToObj(i -> StoreDeliveryPrice.builder()
                .storeId(storeId)
                .searchableDeliveryAddress(anyString())
                .price(Money.wons(((i % 3) + 1) * 1000))
                .build())
            .collect(toList());
    }

}