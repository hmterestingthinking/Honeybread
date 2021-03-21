package com.whatsub.honeybread.core.domain.category;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public void update(Category category) {
        this.name = category.getName();
    }
}
