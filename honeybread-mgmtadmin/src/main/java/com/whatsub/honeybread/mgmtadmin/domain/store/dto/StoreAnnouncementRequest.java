package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.StoreAnnouncement;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Value
public class StoreAnnouncementRequest {

    @NotBlank(message = "소개글은 빈 값이 올 수 없습니다.")
    @Length(min = 50, max = 300, message = "소개글은 50에서 300글자이어야 합니다.")
    String introduce;

    @NotBlank(message = "안내글은 빈 값이 올 수 없습니다.")
    @Length(min = 200, max = 1000, message = "소개글은 200에서 1000글자이어야 합니다.")
    String information;

    @NotBlank(message = "원산지 정보는 빈 값이 올 수 없습니다.")
    @Length(min = 10, max = 500, message = "소개글은 10에서 500글자이어야 합니다.")
    String originCountry;

    public StoreAnnouncement toStoreAnnouncement() {
        return new StoreAnnouncement(introduce, information, originCountry);
    }

}
