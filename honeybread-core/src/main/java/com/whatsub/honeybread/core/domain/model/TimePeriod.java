package com.whatsub.honeybread.core.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public boolean isBefore(LocalDateTime when) {
        return to.isBefore(when);
    }

    @JsonIgnore
    public boolean isAfter(LocalDateTime when) {
        return from.isAfter(when);
    }

    @JsonIgnore
    public boolean include(LocalDateTime when) {
        return isAfterOrEqualsThan(when) && isBeforeOrEqualsThan(when);
    }

    @JsonIgnore
    public boolean isAfterOrEqualsThan(LocalDateTime when) {
        return when.isAfter(from) || when.isEqual(from);
    }

    @JsonIgnore
    public boolean isBeforeOrEqualsThan(LocalDateTime when) { return when.isBefore(to) || when.equals(to); }

    @JsonIgnore
    public boolean isValid() {
        return from != null && to != null && from.isBefore(to) && to.isAfter(from);
    }
}
