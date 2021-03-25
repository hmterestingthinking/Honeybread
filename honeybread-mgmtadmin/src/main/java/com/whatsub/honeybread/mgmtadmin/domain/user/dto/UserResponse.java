package com.whatsub.honeybread.mgmtadmin.domain.user.dto;

import com.whatsub.honeybread.core.domain.user.User;
import lombok.Data;

@Data
public class UserResponse {

    private String email;
    private String phoneNumber;
    private boolean smsAgreement;
    private boolean marketingAgreement;

    public static UserResponse createUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.email = user.getEmail();
        userResponse.marketingAgreement = user.isMarketingAgreement();
        userResponse.phoneNumber = user.getPhoneNumber();
        userResponse.smsAgreement = user.isSmsAgreement();
        return userResponse;
    }

}
