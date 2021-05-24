package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.StoreBasic;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class StoreBasicResponse {
    String name;

    String tel;

    String imageUrl;

    AddressResponse address;

    StoreAnnouncementResponse storeAnnouncement;

    BusinessHoursResponse operationTime;

    BusinessLicenseResponse businessLicense;

    public static StoreBasicResponse of(StoreBasic entity) {
        return StoreBasicResponse
                .builder()
                .name(entity.getName())
                .tel(entity.getTel())
                .imageUrl(entity.getImageUrl())
                .address(AddressResponse.of(entity.getAddress()))
                .storeAnnouncement(StoreAnnouncementResponse.of(entity.getStoreAnnouncement()))
                .operationTime(BusinessHoursResponse.of(entity.getOperationTime()))
                .businessLicense(BusinessLicenseResponse.of(entity.getBusinessLicense()))
                .build();
    }
}
