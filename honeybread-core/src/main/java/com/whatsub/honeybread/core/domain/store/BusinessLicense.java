package com.whatsub.honeybread.core.domain.store;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BusinessLicense {

    @Column(name = "business_license_number")
    private String number;

    @Column(name = "business_license_store_name")
    private String storeName;

}