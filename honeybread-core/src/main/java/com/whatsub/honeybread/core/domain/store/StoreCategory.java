package com.whatsub.honeybread.core.domain.store;


import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "store_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class StoreCategory extends BaseEntity {

    private Long categoryId;

}
