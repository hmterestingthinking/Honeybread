package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.Address;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class AddressResponse {

    String detailAddress;

    public static AddressResponse of(Address address) {
        return AddressResponse
                .builder()
                .detailAddress(address.getDetailAddress())
                .build();
    }

}