package com.whatsub.honeybread.core.domain.user;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity{

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    public static User createUser(String email, String password, String phoneNumber) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.phoneNumber = phoneNumber;
        return user;
    }

    public void encodePassword(String password) {
        this.password = password;
    }
}