package cn.adelyn.blog.auth.service;

import cn.adelyn.blog.auth.pojo.vo.TokenInfoVO;
import cn.adelyn.framework.cache.util.CaffeineCacheUtil;
import cn.adelyn.framework.core.pojo.bo.UserInfoBO;
import cn.adelyn.framework.core.util.RandomIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AbstractLoginService {

    @Autowired
    private TokenService tokenService;

    @Value("${adelyn.blog.auth.login.redirectUrl:https://blog.adelyn.cn/#/manage}")
    private String blogRedirectUrl;


    public String getAuthCode(UserInfoBO userInfoBO) {
        return tokenService.getAuthCode(userInfoBO);
    }

    public String getRedirectUrl(String authCode) {
        return blogRedirectUrl + "?authCode=" + authCode;
    }
}
