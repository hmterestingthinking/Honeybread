package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.Address;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Value
public class AddressRequest {

    @NotBlank
    @Length(min = 20, max = 100)
    String detailAddress;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AddressRequest(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Address toAddress() {
        return new Address(detailAddress);
    }
}
