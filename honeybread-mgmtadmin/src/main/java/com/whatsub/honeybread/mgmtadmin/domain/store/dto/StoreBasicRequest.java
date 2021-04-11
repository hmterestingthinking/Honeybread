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
    @Length(min = 5, max = 30, message = "스토어의 이름은 5에서 30글자이어야 합니다.")
    String name;

    @NotBlank
    @Pattern(regexp = "\\d{3}-(\\d{3}||\\d{4})-\\d{4}", message = "전화번호 형식이 올바르지 않습니다.")
    String tel;

    @NotBlank
    @URL(message = "URL 형식이 올바르지 않습니다.")
    String imageUrl;

    @Valid
    @NotNull(message = "주소 정보는 필수 입력입니다.")
    AddressRequest address;

    @Valid
    @NotNull(message = "안내 정보는 필수 입력입니다.")
    StoreAnnouncementRequest storeIntroduce;

    @Valid
    @NotNull(message = "운영시간 정보는 필수 입력입니다.")
    BusinessHoursRequest operationTime;

    @Valid
    @NotNull(message = "사업자등록 정보는 필수 입력입니다.")
    BusinessLicenseRequest businessLicense;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StoreBasicRequest(final String name,
                             final String tel,
                             final String imageUrl,
                             final AddressRequest address,
                             final StoreAnnouncementRequest storeIntroduce,
                             final BusinessHoursRequest operationTime,
                             final BusinessLicenseRequest businessLicense) {
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
