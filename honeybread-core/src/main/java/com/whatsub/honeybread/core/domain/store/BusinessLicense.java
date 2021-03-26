package com.whatsub.honeybread.core.domain.store;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BusinessLicense {

    @Column(name = "business_license_number")
    private String number;

    @Column(name = "business_license_store_name")
    private String storeName;

}