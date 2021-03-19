package com.whatsub.honeybread.core.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimePeriod implements Serializable {
    public static TimePeriod EMPTY = new TimePeriod();

    private LocalDateTime from;
    private LocalDateTime to;
}
