package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = OrderPriceDeliveryTipValidator.class)
class OrderPriceDeliveryTipValidatorTest {

    final OrderPriceDeliveryTipValidator validator;

    @MockBean
    OrderPriceDeliveryTipRepository repository;

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

    @Test
    @DisplayName("주문금액별 배달팁중 '~ 이상(null)'의 가격범위가 이미 존재한다면 유효성검사 실패")
    void 주문금액별_배달팁중_이미존재하는_가격범위가_있다면_유효성검사_실패() {
        //given
        final OrderPriceDeliveryTip orderPriceDeliveryTip = 주문금액별_배달팁_생성(10000L, null);
        given(repository.existsByStoreIdAndToPriceIsNull(anyLong())).willReturn(true);

        //when
        final ValidationException validationException
            = assertThrows(ValidationException.class, () -> validator.validate(orderPriceDeliveryTip));

        //then
        assertEquals(1, validationException.getErrors().getErrorCount());
    }

    @Test
    @DisplayName("주문금액별 배달팁중 fromPrice, toPrice에 해당하는 가격범위가 이미 존재한다면 유효성검사 실패")
    void 주문금액별_배달팁중_이미존재하는_가격범위가_있다면_유효성검사_실패2() {
        //given
        final OrderPriceDeliveryTip orderPriceDeliveryTip = 주문금액별_배달팁_생성(10000L, 15000L);
        given(repository.getTipByOrderPrice(anyLong(), any(Money.class)))
            .willReturn(Optional.of(mock(OrderPriceDeliveryTip.class)));

        //when
        final ValidationException validationException
            = assertThrows(ValidationException.class, () -> validator.validate(orderPriceDeliveryTip));

        //then
        assertEquals(1, validationException.getErrors().getErrorCount());
    }

    @Test
    void 주문금액별_배달팁중_이미존재하는_가격범위가_없을경우_성공() {
        //given
        final OrderPriceDeliveryTip orderPriceDeliveryTip = mock(OrderPriceDeliveryTip.class);
        이미존재하는_가격범위가_없음();

        //when
        validator.validate(orderPriceDeliveryTip);

        //then
    }

    private void 이미존재하는_가격범위가_없음() {
        given(repository.existsByStoreIdAndToPriceIsNull(anyLong())).willReturn(false);
        given(repository.getTipByOrderPrice(anyLong(), any(Money.class)))
            .willReturn(Optional.empty(), Optional.empty());
    }

    private OrderPriceDeliveryTip 주문금액별_배달팁_생성(final Long fromPrice, final Long toPrice) {
        return OrderPriceDeliveryTip.builder()
            .storeId(1L)
            .fromPrice(Money.wons(fromPrice))
            .toPrice(toPrice == null ? null : Money.wons(toPrice))
            .build();
    }

}