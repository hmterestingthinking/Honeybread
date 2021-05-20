package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTipValidator;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = OrderPriceDeliveryTipValidator.class)
class OrderPriceDeliveryTipValidatorTest {

    final OrderPriceDeliveryTipValidator validator;

    @ParameterizedTest
    @CsvSource(value = {"10000, 12000", "19000, 20000"})
    void 주문금액별_배달팁_유효성검사_성공(final Long fromPrice, final Long toPrice) {
        //given
        final OrderPriceDeliveryTip orderPriceDeliveryTip = 주문금액별_배달팁_생성(fromPrice, toPrice);

        //when
        validator.validate(orderPriceDeliveryTip);

        //then
    }

    @ParameterizedTest
    @CsvSource(value = {"15000, 12000", "20000, 19000"})
    void 주문금액별_배달팁_유효성검사_실패(final Long fromPrice, final Long toPrice) {
        //given
        final OrderPriceDeliveryTip orderPriceDeliveryTip = 주문금액별_배달팁_생성(fromPrice, toPrice);

        //when
        final ValidationException validationException
            = assertThrows(ValidationException.class, () -> validator.validate(orderPriceDeliveryTip));

        //then
        assertEquals(1, validationException.getErrors().getErrorCount());
    }

    private OrderPriceDeliveryTip 주문금액별_배달팁_생성(final Long fromPrice, final Long toPrice) {
        return OrderPriceDeliveryTip.builder()
            .fromPrice(Money.wons(fromPrice))
            .toPrice(Money.wons(toPrice))
            .build();
    }

}