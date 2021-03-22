package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_operations")
public class StoreOperation extends BaseEntity {

    private Long storeId;

    @Enumerated(EnumType.STRING)
    private DayType dayType;

    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "start_at")),
            @AttributeOverride(name = "to", column = @Column(name = "end_at"))})
    private TimePeriod operationPeriod;

}
