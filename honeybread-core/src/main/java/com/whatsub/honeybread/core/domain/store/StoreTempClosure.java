package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class StoreTempClosure {

    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "temp_close_start_at")),
            @AttributeOverride(name = "to", column = @Column(name = "temp_close_end_at"))})
    private TimePeriod tempClosurePeriod;

}
