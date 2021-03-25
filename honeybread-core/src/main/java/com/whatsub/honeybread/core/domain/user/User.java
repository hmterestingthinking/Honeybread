package com.whatsub.honeybread.core.domain.user;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Column(nullable = false)
    private boolean marketingAgreement;

    @Column(nullable = false)
    private boolean smsAgreement;

    public static User createUser(String email, String password, String phoneNumber,
                                  boolean marketingAgreement, boolean smsAgreement) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.phoneNumber = phoneNumber;
        user.marketingAgreement = marketingAgreement;
        user.smsAgreement = smsAgreement;
        return user;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void update(User modifyUser) {
        this.password = modifyUser.getPassword();
        this.phoneNumber = modifyUser.getPhoneNumber();
        this.marketingAgreement = modifyUser.isMarketingAgreement();
        this.smsAgreement = modifyUser.isSmsAgreement();
    }
}