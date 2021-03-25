package com.whatsub.honeybread.mgmtadmin.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class UserModifyRequest {

    @Pattern(regexp = "^.*(?=^.{6,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",
            message = "패스워드는 문자, 숫자, 특수문자가 포함된 6에서 15글자이어야 합니다.")
    private String password;

    @Pattern(regexp = "010-\\d{4}-\\d{4}", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;

    private boolean smsAgreement;
    private boolean marketingAgreement;

}
