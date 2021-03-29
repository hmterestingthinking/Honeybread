package com.whatsub.honeybread.mgmtadmin.domain.user.dto;

import com.whatsub.honeybread.core.domain.user.User;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponse {

    String email;
    String phoneNumber;
    boolean smsAgreement;
    boolean marketingAgreement;

    public static UserResponse createUserResponse(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .marketingAgreement(user.isMarketingAgreement())
                .smsAgreement(user.isSmsAgreement())
                .build();
    }

}
