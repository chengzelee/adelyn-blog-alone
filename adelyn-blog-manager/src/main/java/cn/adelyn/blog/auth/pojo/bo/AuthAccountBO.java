package cn.adelyn.blog.auth.pojo.bo;

import lombok.Data;

/**
 * @author chengze
 * @date 2022/12/20
 * @desc 用于校验的用户信息
 */
@Data
public class AuthAccountBO {
	/**
	 * 全局唯一的id
	 */
	private Long userId;

	private String password;

	private Integer status;
}
