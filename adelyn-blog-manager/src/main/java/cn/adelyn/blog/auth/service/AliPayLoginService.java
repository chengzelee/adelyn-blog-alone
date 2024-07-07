package cn.adelyn.blog.auth.service;

import cn.adelyn.blog.auth.config.AliPayConfig;
import cn.adelyn.blog.auth.pojo.bo.AlipayUserInfoBO;
import cn.adelyn.blog.auth.pojo.vo.TokenInfoVO;
import cn.adelyn.framework.cache.util.CaffeineCacheUtil;
import cn.adelyn.framework.core.pojo.bo.UserInfoBO;
import cn.adelyn.framework.core.util.HttpUtil;
import cn.adelyn.framework.core.util.RandomIdUtil;
import cn.adelyn.framework.core.util.StringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayLoginService extends AbstractLoginService{

    private final AuthAccountService authAccountService;
    private final AliPayConfig aliPayConfig;

    private static final HttpUtil httpUtil = HttpUtil.builder().build();

    public String getAliPayRedirectUrl() {
        String state = RandomIdUtil.getRandomLongString();
        CaffeineCacheUtil.put(state, 1, 1, TimeUnit.MINUTES);

        return "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id="
                + aliPayConfig.getAppId() +
                "&scope=auth_user" + "&state=" + state +
                "&redirect_uri=" + aliPayConfig.getRedirectUrl();
    }

    public String getAuthCode(String state, String aliPayAuthCode) {
        if (!StringUtil.hasLength(state) || !StringUtil.hasLength(aliPayAuthCode) || Objects.isNull(CaffeineCacheUtil.get(state))){
            return "";
        } else {
            CaffeineCacheUtil.remove(state);
        }

        String aliPayUserId = getAliPayUserId(aliPayAuthCode);
        UserInfoBO userInfoBO = authAccountService.getUserInfoByAliPauUserId(aliPayUserId);
        return getAuthCode(userInfoBO);
    }

    private String getAliPayUserId(String authCode) {
        JSONObject reqBody = new JSONObject();
        reqBody.put("authCode", authCode);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        String res = httpUtil.exchange("http://adelyn-alipayapi:3000/api/alipay/getAccessToken", HttpMethod.POST, headers,
                String.class, reqBody.toJSONString());

        log.info("aliApi res: {}", res);

        JSONObject resObj = JSON.parseObject(res);
        String aliPayUserId = resObj.getJSONObject("data").getString("openId");
        return aliPayUserId;
    }


    // 获取accessToken 时已经拿到了三方id，不需要获取详细信息了
    private AlipayUserInfoBO getUserInfoByToken(String accessToken) {
        return null;
    }
}
