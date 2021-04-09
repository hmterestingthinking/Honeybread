package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.StoreAnnouncement;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Value
public class StoreAnnouncementRequest {

    @NotBlank
    @Length(min = 50, max = 300)
    String introduce;

    @NotBlank
    @Length(min = 200, max = 1000)
    String information;

    @NotBlank
    @Length(min = 10, max = 500)
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
