package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.BusinessLicense;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Value
public class BusinessLicenseRequest {

    @NotBlank
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{5}")
    String businessLicenseNumber;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BusinessLicenseRequest(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public BusinessLicense toBusinessLicense() {
        return new BusinessLicense(businessLicenseNumber);
    }

}
