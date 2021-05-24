package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.StoreAnnouncement;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class StoreAnnouncementResponse {

    String introduce;

    String information;

    String originCountry;

    public static StoreAnnouncementResponse of(StoreAnnouncement announcement) {
        return StoreAnnouncementResponse
                .builder()
                .introduce(announcement.getIntroduce())
                .information(announcement.getInformation())
                .originCountry(announcement.getOriginCountry())
                .build();
    }

}
