package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.BusinessLicense;
import lombok.Value;

@Value
public class BusinessLicenseRequest {

    String businessLicenseNumber;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BusinessLicenseRequest(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public BusinessLicense toBusinessLicense() {
        return new BusinessLicense(businessLicenseNumber);
    }

}
