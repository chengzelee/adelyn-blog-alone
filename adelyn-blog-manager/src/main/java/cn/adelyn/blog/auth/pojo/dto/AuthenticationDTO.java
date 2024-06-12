package cn.adelyn.blog.auth.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author chengze
 * @date 2022/12/20
 * @desc 用于登陆传递账号密码
 */
@Data
public class AuthenticationDTO {
	/**
	 * 用户id
	 */
	@NotNull(message = "userName 不能为空")
	private String userName;

	/**
	 * 密码
	 */
	@NotBlank(message = "password 不能为空")
	private String password;
}
