package com.whatsub.honeybread.core.domain.advertisement;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Entity
@Table(name = "advertisements")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class Advertisement extends BaseEntity {

    @Column(nullable = false)
    private AdvertisementType type;

    @Column(nullable = false)
    private int maximumStoreCounts;

    @AttributeOverrides(
        value = {
            @AttributeOverride(name = "from", column = @Column(name = "ad_start_at", nullable = false)),
            @AttributeOverride(name = "to", column = @Column(name = "ad_end_at", nullable = false))
        }
    )
    private TimePeriod period = TimePeriod.EMPTY;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "advertisement")
    private List<AdvertisedStore> advertisedStores = List.of();
}
