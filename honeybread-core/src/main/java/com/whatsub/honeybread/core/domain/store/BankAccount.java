package com.whatsub.honeybread.core.domain.store;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BankAccount {

    @Enumerated(EnumType.STRING)
    private BankType bankType;

    private String accountNumber;

}
