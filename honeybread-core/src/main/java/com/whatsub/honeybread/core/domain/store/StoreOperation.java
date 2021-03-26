package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.*;

import javax.persistence.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class StoreOperation {

    @Enumerated(EnumType.STRING)
    private OperationStatus status;

    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "operation_start_at")),
            @AttributeOverride(name = "to", column = @Column(name = "operation_end_at"))})
    private TimePeriod period;

}
