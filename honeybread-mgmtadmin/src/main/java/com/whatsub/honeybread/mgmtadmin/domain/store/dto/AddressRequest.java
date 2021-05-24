package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.Address;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Value
public class AddressRequest {

    @NotBlank(message = "상세주소는 빈 값이 올 수 없습니다.")
    @Length(min = 20, max = 100, message = "상세주소는 20에서 100글자이어야 합니다.")
    String detailAddress;

    public Address toAddress() {
        return new Address(detailAddress);
    }
}
