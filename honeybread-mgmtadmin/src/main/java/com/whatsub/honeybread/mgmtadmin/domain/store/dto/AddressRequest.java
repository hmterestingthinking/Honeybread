package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.Address;
import lombok.Value;

@Value
public class AddressRequest {

    String detailAddress;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AddressRequest(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Address toAddress() {
        return new Address(detailAddress);
    }
}
