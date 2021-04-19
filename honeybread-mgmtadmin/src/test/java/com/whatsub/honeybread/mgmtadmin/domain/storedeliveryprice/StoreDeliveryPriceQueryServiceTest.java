package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPriceRepository;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceGroupResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
        final List<StoreDeliveryPrice> list = 주소별_배달금액을_Size만큼_생성(size, price);
        모든_주소별_배달금액을_Id로_검색(list);

        //when
        final StoreDeliveryPriceGroupResponse storeDeliveryPriceGroupResponse
            = service.getStoreDeliveryPrices(anyLong());

        //then
        모든_주소별_배달금액이_검색되어야함();
        assertTrue(storeDeliveryPriceGroupResponse.getGroupByPrice().containsKey(price));
        assertEquals(size, storeDeliveryPriceGroupResponse.getGroupByPrice().get(price).size());
    }

    private void 모든_주소별_배달금액이_검색되어야함() {
        then(repository).should().getStoreDeliveryPrices(anyLong());
    }

    private void 모든_주소별_배달금액을_Id로_검색(List<StoreDeliveryPrice> list) {
        given(repository.getStoreDeliveryPrices(anyLong())).willReturn(list);
    }

    private List<StoreDeliveryPrice> 주소별_배달금액을_Size만큼_생성(final int size, final int price) {
        final List<StoreDeliveryPrice> list = IntStream.range(0, size)
            .mapToObj(i -> StoreDeliveryPrice.builder().price(Money.wons(price)).build())
            .collect(Collectors.toList());
        return list;
    }
}