package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipRepository;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = OrderPriceDeliveryTipQueryService.class)
class OrderPriceDeliveryTipQueryServiceTest {

    final OrderPriceDeliveryTipQueryService queryService;

    @MockBean
    OrderPriceDeliveryTipRepository repository;

    @Test
    void StoreId로_전체_주문금액별_배달팁_검색() {
        //given
        int size = 5;
        주문금액별_배달팁_사이즈만큼_생성(size);
        //when
        final List<OrderPriceDeliveryTipResponse> responses = queryService.getAllByStoreId(anyLong());

        //then
        assertEquals(size, responses.size());
    }

    private void 주문금액별_배달팁_사이즈만큼_생성(final int size) {
        final List<OrderPriceDeliveryTip> list = IntStream.range(0, size)
                .mapToObj(ignore -> mock(OrderPriceDeliveryTip.class))
                .collect(Collectors.toList());
        given(repository.findByStoreId(anyLong())).willReturn(list);
    }

}