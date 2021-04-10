package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.BusinessLicense;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Value
public class BusinessLicenseRequest {

    @NotBlank(message = "사업자등록번호는 빈 값이 올 수 없습니다.")
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{5}", message = "사업자등록번호 형식이 올바르지 않습니다.")
    String businessLicenseNumber;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BusinessLicenseRequest(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public BusinessLicense toBusinessLicense() {
        return new BusinessLicense(businessLicenseNumber);
    }

}
