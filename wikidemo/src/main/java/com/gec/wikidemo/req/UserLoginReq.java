package com.gec.wikidemo.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserLoginReq {
//    @NotEmpty(message = "【用户名】不能为空")
    private String loginName;
//    @NotEmpty(message = "【密码】不能为空")
//// @Length(min = 6, max = 20, message = "【密码】6~20位")
//    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message =
//            "【密码】规则不正确")
    private String password;
}
