package com.whatsub.honeybread.core.domain.userstorefavorite;


import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_store_favorites")
public class UserStoreFavorite extends BaseEntity {

    private Long userId;

    private Long storeId;

}