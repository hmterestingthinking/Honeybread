package com.whatsub.honeybread.core.domain.advertisement;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Table(name = "advertisements")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class Advertisement extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
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

    @BatchSize(size = 50)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private List<AdvertisedStore> advertisedStores = List.of();

    @Builder
    private Advertisement(
        AdvertisementType type,
        int maximumStoreCounts,
        TimePeriod period,
        List<AdvertisedStore> advertisedStores
    ) {
        this.type = type;
        this.maximumStoreCounts = maximumStoreCounts;
        this.period = period;
        this.advertisedStores = advertisedStores;
    }
}
