package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.BusinessHours;
import lombok.Value;

@Value
public class BusinessHoursRequest {

    String businessHour;

    String holiday;

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
