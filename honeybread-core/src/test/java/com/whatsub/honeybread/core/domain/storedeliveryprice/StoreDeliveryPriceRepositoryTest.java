package com.whatsub.honeybread.core.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class StoreDeliveryPriceRepositoryTest {

    final StoreDeliveryPriceRepository repository;

    @Test
    void StoreId와_검색가능주소_중복_확인() {
        //given
        StoreDeliveryPrice addressPrice = 주소별_배달금액_생성(1L, "서울시 강남구 수서동", Money.wons(5000));
        repository.save(addressPrice);

        //when
        boolean result =
            repository.existsByStoreIdAndSearchableDeliveryAddress(addressPrice.getStoreId(), addressPrice.getSearchableDeliveryAddress());

        //then
        assertTrue(result);
    }

    private StoreDeliveryPrice 주소별_배달금액_생성(final Long storeId, final String address, final Money price) {
        return StoreDeliveryPrice.builder()
            .storeId(storeId)
            .searchableDeliveryAddress(address)
            .price(price)
            .build();
    }

}