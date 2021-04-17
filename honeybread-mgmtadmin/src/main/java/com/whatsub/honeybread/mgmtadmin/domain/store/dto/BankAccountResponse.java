package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.BankAccount;
import com.whatsub.honeybread.core.domain.store.BankType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class BankAccountResponse {
    BankType bankType;

    String accountNumber;

    public static BankAccountResponse of(BankAccount bankAccount) {
        return BankAccountResponse
                .builder()
                .bankType(bankAccount.getBankType())
                .accountNumber(bankAccount.getAccountNumber())
                .build();
    }

}
