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

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_pay_methos")
public class StorePayMethod extends BaseEntity {

    private Long storeId;

    @Enumerated(EnumType.STRING)
    private PayType payType;

}
