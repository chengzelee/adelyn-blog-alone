package cn.adelyn.blog.auth.pojo.vo;


import lombok.Builder;
import lombok.Data;

/**
 * @Author chengzelee
 * @Date 2022/11/27 17:53
 * @Desc token信息，该信息用户返回给前端，前端请求携带accessToken进行用户校验
 */
@Data
@Builder
public class TokenInfoVO {

	private String accessToken;

	private String refreshToken;

}
