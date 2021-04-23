package com.whatsub.honeybread.core.domain.advertisement.validator;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = AdvertiseBidNoticeValidator.class)
@RequiredArgsConstructor
class AdvertiseBidNoticeValidatorTest {

    static final int 유효한_광고_기간 = 30;
    static final int 유효한_낙찰_가능_스토어_수 = 50;
    static final Money 유효한_입찰_단위_금액 = Money.wons(10_000);

    final AdvertiseBidNoticeValidator validator;

    @Mock
    AdvertisementBidNotice entity;

    @BeforeEach
    void setUp() {
        광고_기간_설정(유효한_광고_기간);
        낙찰_가능_최대_스토어_수_설정(유효한_낙찰_가능_스토어_수);
        입찰_단위_금액_설정(유효한_입찰_단위_금액);
    }

    @ParameterizedTest
    @ValueSource(ints = {
        0, 1, 11, 15, 21, 25, 29
    })
    void 광고_기간이_30일_미만이면_실패(final int adDays) {
        // given
        광고_기간_설정(adDays);

        // when
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(entity));

        // then
        then(entity).should(times(2)).getPeriod();
        assertThat(ex.getErrors().getErrorCount()).isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(ints = {
        30, 31, 41, 55, 61, 75, 89
    })
    void 광고_기간이_30일_이상이라면_성공(final int adDays) {
        // given
        광고_기간_설정(adDays);

        // when
        validator.validate(entity);

        // then
        then(entity).should().getPeriod();
    }

    @ParameterizedTest
    @ValueSource(ints = {
        1, 10, 11, 21, 31, 41, 49
    })
    void 낙찰_가능한_최대_스토어의_수가_50_미만이면_실패(final int maximumStoreCounts) throws Exception {
        // given
        낙찰_가능_최대_스토어_수_설정(maximumStoreCounts);

        // when
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(entity));

        // then
        then(entity).should(times(2)).getMaximumStoreCounts();
        assertThat(ex.getErrors().getErrorCount()).isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(ints = {
        50, 51, 61, 71, 75, 87, 99
    })
    void 낙찰_가능한_최대_스토어의_수가_50_이상이면_성공(final int maximumStoreCounts) throws Exception {
        // given
        낙찰_가능_최대_스토어_수_설정(maximumStoreCounts);

        // when
        validator.validate(entity);

        // then
        then(entity).should().getMaximumStoreCounts();
    }

    @ParameterizedTest
    @ValueSource(ints = {
        100, 1000, 5000, 7000, 9000
    })
    void 입찰_단위_금액이_만원_미만이면_실패(final int bidPriceUnit) throws Exception {
        // given
        입찰_단위_금액_설정(Money.wons(bidPriceUnit));

        // when
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(entity));

        // then
        then(entity).should(times(3)).getBidPriceUnit();
        assertThat(ex.getErrors().getErrorCount()).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(ints = {
        10100, 10500, 11000, 15000, 19999, 111111
    })
    void 입찰_단위가_10_000_원_이_아니라면_실패(final int bidPriceUnit) throws Exception {
        // given
        입찰_단위_금액_설정(Money.wons(bidPriceUnit));

        // when
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(entity));

        // then
        then(entity).should(times(2)).getBidPriceUnit();
        assertThat(ex.getErrors().getErrorCount()).isEqualTo(1);
    }

    private void 광고_기간_설정(int adDays) {
        LocalDateTime from = LocalDateTime.of(2021, 4, 3, 12, 0, 0);
        LocalDateTime to = from.plusDays(adDays);
        given(entity.getPeriod()).willReturn(TimePeriod.of(from, to));
    }

    private void 낙찰_가능_최대_스토어_수_설정(int maximumStoreCounts) {
        given(entity.getMaximumStoreCounts()).willReturn(maximumStoreCounts);
    }

    private void 입찰_단위_금액_설정(Money bidPriceUnit) {
        given(entity.getBidPriceUnit()).willReturn(bidPriceUnit);
    }
}