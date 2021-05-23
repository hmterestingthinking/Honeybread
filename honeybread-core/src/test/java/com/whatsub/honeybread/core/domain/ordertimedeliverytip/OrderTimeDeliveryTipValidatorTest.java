package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.infra.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = OrderTimeDeliveryTipValidator.class)
class OrderTimeDeliveryTipValidatorTest {

    final OrderTimeDeliveryTipValidator validator;

    @MockBean
    OrderTimeDeliveryTipRepository repository;

    @ParameterizedTest
    @CsvSource(value = {"20, 23", "20, 5", "0, 3"})
    void 시간별_배달팁_시간간격_유효성검사_성공(final int fromTime, final int toTime) {
        //given
        final OrderTimeDeliveryTip orderTimeDeliveryTip = 시간별_배달팁_생성(fromTime, toTime);

        //when
        validator.validate(orderTimeDeliveryTip);

        //then

    }

    @ParameterizedTest
    @DisplayName("toTime이나 fromTime이 20시 미만, 또는 9시 이상일 경우 유효성검사 실패")
    @CsvSource(value = {"19, 23", "0, 9"})
    void 시간별_배달팁_시간_유효성검사_실패(final int fromTime, final int toTime) {
        //given
        final OrderTimeDeliveryTip orderTimeDeliveryTip = 시간별_배달팁_생성(fromTime, toTime);

        //when
        final ValidationException validationException
            = assertThrows(ValidationException.class, () -> validator.validate(orderTimeDeliveryTip));

        //then
        assertEquals(1, validationException.getErrors().getErrorCount());
    }

    @ParameterizedTest
    @CsvSource(value = {"0, 23", "3, 1"})
    void 시간별_배달팁_시간간격_유효성검사_실패(final int fromTime, final int toTime) {
        //given
        final OrderTimeDeliveryTip orderTimeDeliveryTip = 시간별_배달팁_생성(fromTime, toTime);

        //when
        final ValidationException validationException
            = assertThrows(ValidationException.class, () -> validator.validate(orderTimeDeliveryTip));

        //then
        assertEquals(1, validationException.getErrors().getErrorCount());
    }

    private OrderTimeDeliveryTip 시간별_배달팁_생성(final int hourOfFromTime, final int hourOfToTime) {
        return OrderTimeDeliveryTip.builder()
            .deliveryTimePeriod(DeliveryTimePeriod.builder()
                .from(LocalTime.of(hourOfFromTime, 0))
                .to(LocalTime.of(hourOfToTime, 0))
                .build())
            .build();
    }

}