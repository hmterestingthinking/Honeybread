package com.whatsub.honeybread.core.domain.store;


import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
public class StorePayMethod {

    @Enumerated(EnumType.STRING)
    private PayType payType;

}
