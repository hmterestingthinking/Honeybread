package com.whatsub.honeybread.mgmtadmin.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserModifyRequest {

    @NotEmpty
    private String password;
    private String phoneNumber;
    private boolean smsAgreement;
    private boolean marketingAgreement;

}
