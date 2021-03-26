package com.whatsub.honeybread.mgmtadmin.domain.user.dto;

import com.whatsub.honeybread.core.domain.user.User;
import io.swagger.annotations.ApiModel;
import lombok.Value;

import javax.validation.constraints.Pattern;

@ApiModel("유저 수정사항 요청")
@Value
public class UserModifyRequest {

    @Pattern(regexp = "^.*(?=^.{6,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",
            message = "패스워드는 문자, 숫자, 특수문자가 포함된 6에서 15글자이어야 합니다.")
    String password;

    @Pattern(regexp = "010-\\d{4}-\\d{4}", message = "전화번호 형식이 올바르지 않습니다.")
    String phoneNumber;

    boolean smsAgreement;
    boolean marketingAgreement;

    public User toUser(String origin) {
        return User.createUser(origin, this.password, this.phoneNumber, this.marketingAgreement, this.smsAgreement);
    }

}
