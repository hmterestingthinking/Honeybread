package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.BusinessHours;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Value
public class BusinessHoursRequest {

    @NotBlank(message = "운영시간 정보는 빈 값이 올 수 없습니다.")
    @Length(max = 100, message = "운영시간 정보는 최대 100글자입니다.")
    String businessHour;

    @Length(max = 100, message = "운영시간 정보는 최대 100글자입니다.")
    String holiday;

    @Length(max = 100, message = "운영시간 정보는 최대 100글자입니다.")
    String breakTime;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BusinessHoursRequest(final String businessHour, final String holiday, final String breakTime) {
        this.businessHour = businessHour;
        this.holiday = holiday;
        this.breakTime = breakTime;
    }

    public BusinessHours toBusinessHours() {
        return new BusinessHours(businessHour, holiday, breakTime);
    }

}
