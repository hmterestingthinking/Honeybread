package com.whatsub.honeybread.core.domain.advertisement.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.whatsub.honeybread.core.domain.advertisement.Advertisement;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementSearch;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.whatsub.honeybread.core.domain.advertisement.QAdvertisement.advertisement;

public class AdvertisementRepositoryImpl extends QuerydslRepositorySupport implements AdvertisementRepositoryCustom {

    public AdvertisementRepositoryImpl() {
        super(Advertisement.class);
    }

    @Override
    public Page<Advertisement> findAll(final Pageable pageable, final AdvertisementSearch search) {
        return applyPagination(pageable,
            query ->
                query
                    .from(advertisement)
                    .where(
                        eqType(search.getType()),
                        containsPeriod(search.getPeriod())
                    )
        );
    }

    private BooleanExpression eqType(final AdvertisementType type) {
        return type == null ? null : advertisement.type.eq(type);
    }

    private BooleanExpression containsPeriod(final TimePeriod period) {
        if (period == null || !period.isValid()) {
            return null;
        }
        return advertisement.period.to.goe(period.getFrom())
            .and(advertisement.period.from.loe(period.getTo()));
    }
}
