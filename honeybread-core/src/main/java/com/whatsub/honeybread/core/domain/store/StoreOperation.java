package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_operations")
public class StoreOperation extends BaseEntity {

    private Long storeId;

    @Enumerated(EnumType.STRING)
    private DayType dayType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

}
