package com.whatsub.honeybread.core.domain.store;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class BankAccount {

    @Enumerated(EnumType.STRING)
    private BankType bankType;

    private String accountNumber;

}
