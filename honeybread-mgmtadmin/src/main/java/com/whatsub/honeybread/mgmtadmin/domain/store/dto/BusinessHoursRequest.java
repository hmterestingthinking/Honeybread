package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.BusinessHours;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Value
public class BusinessHoursRequest {

    @NotBlank
    @Length(max = 100)
    String businessHour;

    @Length(max = 100)
    String holiday;

    @Length(max = 100)
    String breakTime;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BusinessHoursRequest(String businessHour, String holiday, String breakTime) {
        this.businessHour = businessHour;
        this.holiday = holiday;
        this.breakTime = breakTime;
    }

    public BusinessHours toBusinessHours() {
        return new BusinessHours(businessHour, holiday, breakTime);
    }

}
