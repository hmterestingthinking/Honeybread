package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.BankAccount;
import com.whatsub.honeybread.core.domain.store.BankType;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
public class BankAccountRequest {

    @NotNull(message = "은행정보는 필수 입력입니다.")
    BankType bankType;

    @NotBlank(message = "계좌번호는 빈 값이 올 수 없습니다.")
    @Pattern(regexp = "^([[0-9]]{10,50})$", message = "계좌번호는 숫자와 -를 제외하고는 입력하실 수 없습니다.")
    String accountNumber;

    public BankAccount toBankAccount() {
        return new BankAccount(bankType, accountNumber);
    }

}
