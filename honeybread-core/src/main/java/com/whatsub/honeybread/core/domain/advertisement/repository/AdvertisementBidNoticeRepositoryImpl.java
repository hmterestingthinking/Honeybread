package com.whatsub.honeybread.core.domain.advertisement.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementBidNoticeSearch;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.whatsub.honeybread.core.domain.advertisement.QAdvertisementBidNotice.advertisementBidNotice;

public class AdvertisementBidNoticeRepositoryImpl
    extends QuerydslRepositorySupport implements AdvertisementBidNoticeRepositoryCustom {

    public AdvertisementBidNoticeRepositoryImpl() {
        super(AdvertisementBidNotice.class);
    }

    @Override
    public Page<AdvertisementBidNotice> findAll(Pageable pageable, AdvertisementBidNoticeSearch search) {
        return applyPagination(pageable,
            query ->
                query
                    .from(advertisementBidNotice)
                    .where(
                        eqStatus(search.getStatus()),
                        eqType(search.getType()),
                        containsPeriod(search.getPeriod())
                    )
        );
    }

    private BooleanExpression eqStatus(final AdvertisementBidNotice.Status status) {
        return status == null ? null : advertisementBidNotice.status.eq(status);
    }

    private BooleanExpression eqType(final AdvertisementType type) {
        return type == null ? null : advertisementBidNotice.type.eq(type);
    }

    private BooleanExpression containsPeriod(final TimePeriod period) {
        if (period == null || !period.isValid()) {
            return null;
        }
        return advertisementBidNotice.period.to.goe(period.getFrom())
            .and(advertisementBidNotice.period.from.loe(period.getTo()));
    }
}
