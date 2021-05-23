package com.whatsub.honeybread.core.domain.advertisement.repository;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisedStore;
import com.whatsub.honeybread.core.domain.advertisement.Advertisement;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementSearch;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
@RequiredArgsConstructor
class AdvertisementRepositoryTest {
    final AdvertisementRepository repository;

    final int size = 10;

    @Test
    void 검색조건이_없다면_SIZE_만큼_광고가_조회된다() throws Exception {
        // given
        광고_데이터_등록(size);

        // when
        final Page<Advertisement> advertisementPage = 모든_광고_검색();

        // then
        assertThat(advertisementPage.getNumberOfElements()).isEqualTo(size);
    }

    @Test
    void 울트라콜_광고_조회시_해당하는_광고만_조회된다() throws Exception {
        // given
        광고_데이터_등록(9);
        울트라콜_광고_등록();

        // when
        final Page<Advertisement> advertisementPage = 울트라콜_광고_검색();

        // then
        assertThat(advertisementPage.getNumberOfElements()).isEqualTo(1);
        assertThat(advertisementPage.getContent().get(0).getType()).isEqualTo(AdvertisementType.ULTRA_CALL);
    }


    /**
     * Given
     */
    private List<Advertisement> 광고_데이터_등록(final int size) {
        final List<Advertisement> advertisements = IntStream.range(0, size)
            .mapToObj(value -> 광고_생성(AdvertisementType.OPEN_LIST, null))
            .collect(Collectors.toList());
        return repository.saveAll(advertisements);
    }

    private void 울트라콜_광고_등록() {
        repository.save(광고_생성(AdvertisementType.ULTRA_CALL, null));
    }

    /**
     * When
     */
    private Page<Advertisement> 모든_광고_검색() {
        return repository.findAll(PageRequest.of(0, size), new AdvertisementSearch());
    }

    private Page<Advertisement> 울트라콜_광고_검색() {
        final AdvertisementSearch search = new AdvertisementSearch();
        search.setType(AdvertisementType.ULTRA_CALL);
        return repository.findAll(PageRequest.of(0, size), search);
    }

    /**
     * Then
     */

    /**
     * Helper
     */
    private Advertisement 광고_생성(final AdvertisementType type, TimePeriod period) {
        if (period == null) {
            period = TimePeriod.of(
                LocalDateTime.of(2021, 4, 12, 12, 0, 0),
                LocalDateTime.of(2021, 6, 12, 12, 0, 0)
            );
        }

        return Advertisement.builder()
            .type(type)
            .maximumStoreCounts(50)
            .period(period)
            .advertisedStores(
                List.of(
                    광고_대상_스토어_생성(1L),
                    광고_대상_스토어_생성(2L),
                    광고_대상_스토어_생성(3L),
                    광고_대상_스토어_생성(4L),
                    광고_대상_스토어_생성(5L)
                )
            )
            .build();
    }

    private AdvertisedStore 광고_대상_스토어_생성(final Long storeId) {
        return AdvertisedStore.builder()
            .storeId(storeId)
            .winningBidPrice(Money.wons(100_000))
            .build();
    }
}