package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.BusinessLicense;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class BusinessLicenseResponse {

    String businessLicenseNumber;

    public static BusinessLicenseResponse of(BusinessLicense businessLicense) {
        return BusinessLicenseResponse
                .builder()
                .businessLicenseNumber(businessLicense.getBusinessLicenseNumber())
                .build();
    }
}
