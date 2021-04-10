package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.StoreBasic;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
public class StoreBasicRequest {

    @NotBlank
    @Length(min = 5, max = 30)
    String name;

    @NotBlank
    @Pattern(regexp = "\\d{3}-(\\d{3}||\\d{4})-\\d{4}", message = "전화번호 형식이 올바르지 않습니다.")
    String tel;

    @NotBlank
    @URL
    String imageUrl;

    @Valid
    @NotNull
    AddressRequest address;

    @Valid
    @NotNull
    StoreAnnouncementRequest storeIntroduce;

    @Valid
    @NotNull
    BusinessHoursRequest operationTime;

    @Valid
    @NotNull
    BusinessLicenseRequest businessLicense;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StoreBasicRequest(String name,
                             String tel,
                             String imageUrl,
                             AddressRequest address,
                             StoreAnnouncementRequest storeIntroduce,
                             BusinessHoursRequest operationTime,
                             BusinessLicenseRequest businessLicense) {
        this.name = name;
        this.tel = tel;
        this.imageUrl = imageUrl;
        this.address = address;
        this.storeIntroduce = storeIntroduce;
        this.operationTime = operationTime;
        this.businessLicense = businessLicense;
    }

    public StoreBasic toStoreBasic() {
        return new StoreBasic(name,
                tel,
                imageUrl,
                address.toAddress(),
                storeIntroduce.toStoreAnnouncement(),
                operationTime.toBusinessHours(),
                businessLicense.toBusinessLicense()
        );
    }
}
