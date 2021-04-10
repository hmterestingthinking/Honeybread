package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.StoreBasic;
import lombok.Value;

@Value
public class StoreBasicRequest {

    String name;

    String tel;

    String imageUrl;

    AddressRequest address;

    StoreAnnouncementRequest storeIntroduce;

    BusinessHoursRequest operationTime;

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
