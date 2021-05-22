package com.whatsub.honeybread.core.domain.advertisement.repository;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementBidNoticeSearch;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
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
class AdvertisementBidNoticeRepositoryTest {
    final AdvertisementBidNoticeRepository repository;

    final int size = 10;

    @Test
    void 검색조건이_없다면_SIZE_만큼_스토어가_조회된다() {
        // given
        입찰공고_데이터_등록(size);

        // when
        Page<AdvertisementBidNotice> bidNoticePage = 모든_입찰공고_검색();

        // then
        assertThat(bidNoticePage.getNumberOfElements()).isEqualTo(size);
    }


    @Test
    void 종료된_공고_조회시_해당하는_공고만_조회된다() {
        // given
        List<AdvertisementBidNotice> advertisementBidNotices = 입찰공고_데이터_등록(size);
        advertisementBidNotices.get(0).close();

        // when
        Page<AdvertisementBidNotice> bidNoticePage = 종료된_공고_검색();

        // then
        assertThat(bidNoticePage.getNumberOfElements()).isEqualTo(1);
        assertThat(bidNoticePage.getContent().get(0).getStatus()).isEqualTo(AdvertisementBidNotice.Status.CLOSED);
    }

    @Test
    void 울트라콜_공고_조회시_해당하는_공고만_조회된다() {
        // given
        입찰공고_데이터_등록(9);
        울트라콜_입찰공고_데이터_등록();

        // when
        Page<AdvertisementBidNotice> bidNoticePage = 울트라콜_공고_검색();

        // then
        assertThat(bidNoticePage.getNumberOfElements()).isEqualTo(1);
        assertThat(bidNoticePage.getContent().get(0).getType()).isEqualTo(AdvertisementType.ULTRA_CALL);
    }

    @DisplayName("1월 ~ 3월 30일 내에 노출되는 공고 조회")
    @Test
    void 기간검색시_해당기간내에_노출되는_공고가_조회딘다() throws Exception {
        // given
        입찰공고_데이터_등록(8);

        repository.save(입찰공고_생성(AdvertisementType.ULTRA_CALL,
            TimePeriod.of(
                LocalDateTime.of(2021, 3, 12, 12, 0, 0),
                LocalDateTime.of(2021, 4, 12, 12, 0, 0)
            )
        ));

        repository.save(입찰공고_생성(AdvertisementType.ULTRA_CALL,
            TimePeriod.of(
                LocalDateTime.of(2021, 1, 12, 12, 0, 0),
                LocalDateTime.of(2021, 3, 12, 12, 0, 0)
            )
        ));

        // when
        Page<AdvertisementBidNotice> bidNoticePage = _1월_3월말_노출되는_공고_검색();

        // then
        assertThat(bidNoticePage.getNumberOfElements()).isEqualTo(2);
    }


    /**
     * Given
     */
    private List<AdvertisementBidNotice> 입찰공고_데이터_등록(final int size) {
        List<AdvertisementBidNotice> bidNotices = IntStream.range(0, size)
            .mapToObj(value -> 입찰공고_생성(AdvertisementType.OPEN_LIST, null))
            .collect(Collectors.toList());
        return repository.saveAll(bidNotices);
    }

    private void 울트라콜_입찰공고_데이터_등록() {
        repository.save(입찰공고_생성(AdvertisementType.ULTRA_CALL, null));
    }

    /**
     * When
     */
    private Page<AdvertisementBidNotice> 입찰공고_검색(PageRequest pageRequest, AdvertisementBidNoticeSearch search) {
        return repository.findAll(pageRequest, search);
    }

    private Page<AdvertisementBidNotice> 종료된_공고_검색() {
        PageRequest pageRequest = PageRequest.of(0, size);
        AdvertisementBidNoticeSearch search = new AdvertisementBidNoticeSearch();
        search.setStatus(AdvertisementBidNotice.Status.CLOSED);

        Page<AdvertisementBidNotice> bidNoticePage = 입찰공고_검색(pageRequest, search);
        return bidNoticePage;
    }

    private Page<AdvertisementBidNotice> 울트라콜_공고_검색() {
        PageRequest pageRequest = PageRequest.of(0, size);
        AdvertisementBidNoticeSearch search = new AdvertisementBidNoticeSearch();
        search.setType(AdvertisementType.ULTRA_CALL);

        Page<AdvertisementBidNotice> bidNoticePage = 입찰공고_검색(pageRequest, search);
        return bidNoticePage;
    }

    private Page<AdvertisementBidNotice> _1월_3월말_노출되는_공고_검색() {
        PageRequest pageRequest = PageRequest.of(0, size);
        AdvertisementBidNoticeSearch search = new AdvertisementBidNoticeSearch();
        search.setPeriod(
            TimePeriod.of(
                LocalDateTime.of(2021, 1, 1, 12, 0, 0),
                LocalDateTime.of(2021, 3, 30, 12, 0, 0)
            )
        );

        Page<AdvertisementBidNotice> bidNoticePage = 입찰공고_검색(pageRequest, search);
        return bidNoticePage;
    }

    private Page<AdvertisementBidNotice> 모든_입찰공고_검색() {
        PageRequest pageRequest = PageRequest.of(0, size);
        AdvertisementBidNoticeSearch search = new AdvertisementBidNoticeSearch();

        Page<AdvertisementBidNotice> bidNoticePage = 입찰공고_검색(pageRequest, search);
        return bidNoticePage;
    }


    /**
     * Helper
     */
    private AdvertisementBidNotice 입찰공고_생성(final AdvertisementType type, TimePeriod period) {
        if (period == null) {
            period = TimePeriod.of(
                LocalDateTime.of(2021, 4, 12, 12, 0, 0),
                LocalDateTime.of(2021, 6, 12, 12, 0, 0)
            );
        }
        return AdvertisementBidNotice.builder()
            .type(type)
            .maximumStoreCounts(50)
            .minimumBidPrice(Money.wons(100_000))
            .bidPriceUnit(Money.wons(100_000))
            .period(period)
            .build();
    }
}