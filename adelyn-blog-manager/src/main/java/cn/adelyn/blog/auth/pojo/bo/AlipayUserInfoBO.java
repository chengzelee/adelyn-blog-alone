package cn.adelyn.blog.auth.pojo.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlipayUserInfoBO {
    private String openId;
    private String userName;
    private String nickName;
}
