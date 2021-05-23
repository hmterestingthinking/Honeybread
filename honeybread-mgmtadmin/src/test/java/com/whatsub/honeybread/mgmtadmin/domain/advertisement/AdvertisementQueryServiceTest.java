package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisedStore;
import com.whatsub.honeybread.core.domain.advertisement.Advertisement;
import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementSearch;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementRepository;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = AdvertisementQueryService.class)
@RequiredArgsConstructor
class AdvertisementQueryServiceTest {

    final AdvertisementQueryService queryService;

    final int size = 10;
    final int maximumStoreCounts = 30;

    @MockBean
    AdvertisementRepository repository;

    @Test
    void 검색_요청시_검색조건이_없다면_SIZE_만큼_조회_성공() throws Exception {
        // given
        광고목록_검색시_성공한다();

        // when
        final Page<AdvertisementResponse> advertisementPage = 광고목록_검색();

        // then
        광고목록_검색이_수행되어야_한다();
        assertThat(advertisementPage.getNumberOfElements()).isEqualTo(size);
    }

    /**
     * Given
     */
    private void 광고목록_검색시_성공한다() {
        final List<Advertisement> advertisements = 광고_목록_생성();
        given(repository.findAll(any(Pageable.class), any(AdvertisementSearch.class)))
            .willReturn(new PageImpl<>(advertisements, PageRequest.of(0, size), size));
    }

    /**
     * When
     */
    private Page<AdvertisementResponse> 광고목록_검색() {
        final PageRequest pageRequest = PageRequest.of(0, size);
        final AdvertisementSearch search = new AdvertisementSearch();
        return queryService.getAdvertisements(pageRequest, search);
    }

    /**
     * Then
     */
    private void 광고목록_검색이_수행되어야_한다() {
        then(repository).should().findAll(any(Pageable.class), any(AdvertisementSearch.class));
    }

    /**
     * Helper
     */
    private List<Advertisement> 광고_목록_생성() {
        return LongStream.range(0, size)
            .mapToObj(this::광고_생성)
            .collect(Collectors.toList());
    }

    private Advertisement 광고_생성(final long id) {
        final Advertisement mock = mock(Advertisement.class);
        given(mock.getId()).willReturn(id);
        given(mock.getMaximumStoreCounts()).willReturn(maximumStoreCounts);
        given(mock.getPeriod()).willReturn(TimePeriod.of(
            LocalDateTime.of(2021, 5, 16, 12, 0, 0),
            LocalDateTime.of(2021, 6, 16, 12, 0 ,0)
        ));

        final List<AdvertisedStore> advertisedStores = LongStream.range(0, maximumStoreCounts)
            .mapToObj(v -> 광고_대상_스토어_생성(id + v, v))
            .collect(Collectors.toList());
        given(mock.getAdvertisedStores()).willReturn(advertisedStores);

        return mock;
    }

    private AdvertisedStore 광고_대상_스토어_생성(final long id, final long storeId) {
        final AdvertisedStore mock = mock(AdvertisedStore.class);
        given(mock.getId()).willReturn(id);
        given(mock.getStoreId()).willReturn(storeId);
        given(mock.getWinningBidPrice()).willReturn(Money.wons(100_000));
        given(mock.getCreatedAt()).willReturn(LocalDateTime.of(2021, 4, 16, 12, 0 ,0));
        given(mock.getLastModifiedAt()).willReturn(LocalDateTime.of(2021, 4, 16, 12, 0 ,0));
        return mock;
    }
}