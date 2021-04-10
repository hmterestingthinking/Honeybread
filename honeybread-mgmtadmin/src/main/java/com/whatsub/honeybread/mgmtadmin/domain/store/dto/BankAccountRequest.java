package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.BankAccount;
import com.whatsub.honeybread.core.domain.store.BankType;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class BankAccountRequest {

    @NotNull
    BankType bankType;

    @NotBlank
    String accountNumber;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BankAccountRequest(BankType bankType, String accountNumber) {
        this.bankType = bankType;
        this.accountNumber = accountNumber;
    }

    public BankAccount toBankAccount() {
        return new BankAccount(bankType, accountNumber);
    }

}
