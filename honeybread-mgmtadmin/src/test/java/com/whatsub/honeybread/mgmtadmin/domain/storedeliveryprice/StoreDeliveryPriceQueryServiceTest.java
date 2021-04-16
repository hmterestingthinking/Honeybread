package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPriceRepository;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = StoreDeliveryPriceQueryService.class)
@RequiredArgsConstructor
class StoreDeliveryPriceQueryServiceTest {

    final StoreDeliveryPriceQueryService service;

    @MockBean
    StoreDeliveryPriceRepository repository;

    @Test
    void StoreId로_모든_주소별_배달금액_검색() {
        //given
        final int size = 10;
        final int price = 1000;
        final Map<Money, List<StoreDeliveryPrice>> map = 주소별_배달금액을_Size만큼_생성(size, price);
        모든_주소별_배달금액을_Id로_검색(map);

        //when
        final Map<Integer, List<StoreDeliveryPriceResponse>> storeDeliveryPrices = service.getStoreDeliveryPrices(anyLong());

        //then
        모든_주소별_배달금액이_검색되어야함();
        assertTrue(storeDeliveryPrices.containsKey(price));
        assertEquals(size, storeDeliveryPrices.get(price).size());
    }

    private void 모든_주소별_배달금액이_검색되어야함() {
        then(repository).should().getStoreDeliveryPrices(anyLong());
    }

    private void 모든_주소별_배달금액을_Id로_검색(final Map<Money, List<StoreDeliveryPrice>> map) {
        given(repository.getStoreDeliveryPrices(anyLong())).willReturn(map);
    }

    private Map<Money, List<StoreDeliveryPrice>> 주소별_배달금액을_Size만큼_생성(final int size, final int price) {
        final List<StoreDeliveryPrice> collect = IntStream.range(0, size)
            .mapToObj(i -> mock(StoreDeliveryPrice.class))
            .collect(Collectors.toList());

        Map<Money, List<StoreDeliveryPrice>> map = new HashMap<>();
        map.put(Money.wons(price), collect);
        return map;
    }
}