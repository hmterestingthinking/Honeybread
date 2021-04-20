package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipRepository;
import com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto.OrderPriceDeliveryTipRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = OrderPriceDeliveryTipService.class)
class OrderPriceDeliveryTipServiceTest {

    final OrderPriceDeliveryTipService service;

    @MockBean
    OrderPriceDeliveryTipRepository repository;

    @Mock
    OrderPriceDeliveryTip orderPriceDeliveryTip;

    @Test
    void 주문가격별_배달팁_생성() {
        //given
        final OrderPriceDeliveryTipRequest request = 주문가격별_배달팁_요청_생성();

        //when
        service.create(request);
        
        //then
        주문가격별_배달팁이_생성되어야함();
        주문가격별_배달팁_From_To가_존재하는지_확인되어야함();
    }

    private void 주문가격별_배달팁_From_To가_존재하는지_확인되어야함() {
        then(repository).should().existsByFromAndTo(any(Money.class), any(Money.class));
    }

    private void 주문가격별_배달팁이_생성되어야함() {
        then(repository).should().save(any(OrderPriceDeliveryTip.class));
    }

    private OrderPriceDeliveryTipRequest 주문가격별_배달팁_요청_생성(){
        return new OrderPriceDeliveryTipRequest(1L, Money.wons(10000), Money.wons(15000), Money.wons(1000));
    }

}