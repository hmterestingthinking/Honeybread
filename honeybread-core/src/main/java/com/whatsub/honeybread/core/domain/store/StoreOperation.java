package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class StoreOperation {

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_status")
    private OperationStatus status;

    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "operation_start_at")),
            @AttributeOverride(name = "to", column = @Column(name = "operation_end_at"))})
    private TimePeriod period;

}
