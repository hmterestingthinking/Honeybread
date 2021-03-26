package com.whatsub.honeybread.core.domain.store;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class Address {

    private String zipNo;

    private String lnbrMnnm;

    @Column(name = "address_detail")
    private String detail;

}
