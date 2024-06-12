package cn.adelyn.blog.auth.pojo.dto;

import cn.adelyn.framework.core.util.PrincipalUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterAccountDTO {

    @NotBlank(message = "用户名 不能为空~")
    @Pattern(regexp = PrincipalUtil.USER_NAME_REGEXP, message = "用户名应为数字、字母、下划线组成,且为4-16位,不能为纯数字")
    private String userName;

    @NotBlank(message = "账号密码 不能为空~")
    private String password;
}
