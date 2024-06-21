/*
package cn.adelyn.blog.auth.service;

import cn.adelyn.blog.auth.pojo.bo.AlipayUserInfoBO;
import cn.adelyn.framework.core.execption.AdelynException;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayLoginService {

    */
/**Alipay客户端*//*

    private final AlipayClient alipayClient;

    public String getAccessToken(String authCode) {
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(authCode);
        request.setGrantType("authorization_code");
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
            return oauthTokenResponse.getAccessToken();
        } catch (AlipayApiException e) {
            throw new AdelynException(e);
        }
    }

    */
/**
     * 根据access_token获取用户信息
     * @param token
     * @return
     *//*

    public AlipayUserInfoBO getUserInfoByToken(String token) {
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest ();
        AlipayUserInfoShareResponse response = null;

        try {
            response = alipayClient.execute(request, token);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

        if (response.isSuccess()) {
            return AlipayUserInfoBO.builder()
                .openId(response.getOpenId())
                .userName(response.getUserName())
                .nickName(response.getNickName())
                .build();
        } else {
            throw new RuntimeException("get aliPay userInfo err");
        }
    }
}
*/
