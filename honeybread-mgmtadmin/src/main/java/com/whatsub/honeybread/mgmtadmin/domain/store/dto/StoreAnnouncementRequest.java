package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.StoreAnnouncement;
import lombok.Value;

@Value
public class StoreAnnouncementRequest {

    String introduce;

    String information;

    String originCountry;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StoreAnnouncementRequest(String introduce, String information, String originCountry) {
        this.introduce = introduce;
        this.information = information;
        this.originCountry = originCountry;
    }

    public StoreAnnouncement toStoreAnnouncement() {
        return new StoreAnnouncement(introduce, information, originCountry);
    }

}
