package com.whatsub.honeybread.core.domain.recentaddress;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recent_delivery_address")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class RecentDeliveryAddress extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private String searchableDeliveryAddress;

    @Column(nullable = false)
    private String stateNameAddress;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private LocalDateTime usedAt;

    @Builder
    public RecentDeliveryAddress (Long userId,
                                  String deliveryAddress,
                                  String searchableDeliveryAddress,
                                  String stateNameAddress,
                                  String zipCode,
                                  LocalDateTime usedAt) {
        this.userId = userId;
        this.deliveryAddress = deliveryAddress;
        this.searchableDeliveryAddress = searchableDeliveryAddress;
        this.stateNameAddress = stateNameAddress;
        this.zipCode = zipCode;
        this.usedAt = usedAt;
    }
}