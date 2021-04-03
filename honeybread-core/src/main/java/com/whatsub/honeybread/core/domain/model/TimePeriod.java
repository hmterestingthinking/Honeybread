package com.whatsub.honeybread.core.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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

    private TimePeriod(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public static TimePeriod of(LocalDateTime from, LocalDateTime to) {
        return new TimePeriod(from, to);
    }
}
