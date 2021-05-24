package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.BusinessHours;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class BusinessHoursResponse {
    String businessHour;

    String holiday;

    String breakTime;

    public static BusinessHoursResponse of(BusinessHours businessHours) {
        return BusinessHoursResponse
                .builder()
                .businessHour(businessHours.getBusinessHour())
                .holiday(businessHours.getHoliday())
                .breakTime(businessHours.getBreakTime())
                .build();
    }
}
