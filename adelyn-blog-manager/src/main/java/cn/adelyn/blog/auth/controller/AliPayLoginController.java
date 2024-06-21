/*
package cn.adelyn.blog.auth.controller;

import cn.adelyn.blog.auth.pojo.bo.AlipayUserInfoBO;
import cn.adelyn.blog.auth.service.AliPayLoginService;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth/aliPay")
public class AliPayLoginController {

    private final AliPayLoginService aliPayLoginService;

    @RequestMapping("/public/login")
    public void login(HttpServletResponse resp) throws IOException {
        String id="你的appid";
        String url="你的回调路径";
        // 一次性的，防csrf
        // https://opendocs.alipay.com/support/04z2xy?pathHash=63149ad4
        String state = "";
        resp.sendRedirect("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id="+id+"&scope=auth_user"+"&state=init"+"&redirect_uri="+url);
    }

    @RequestMapping("/callBack/login")
    public String getAuthCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("alipay callBack params: {}", request.getParameterMap());
        //从request中获取授权信息
        String authCode = request.getParameter("auth_code");
        String appID = request.getParameter("app_id");
        String scope = request.getParameter("scope");
        String state = "";
        // todo 校验state有效性

        if (StringUtils.hasLength(authCode)) {
            //获取access_token
            String accessToken = aliPayLoginService.getAccessToken(authCode);
            //获取用户信息
            if (StringUtils.hasLength(accessToken)) {
                //获取用户信息
                AlipayUserInfoBO alipayUserInfo = aliPayLoginService.getUserInfoByToken(accessToken);
                log.info("aliPay userInfo: {}", JSON.toJSONString(alipayUserInfo));
                // 查用户，拿token
                //带token跳转主页
                response.addHeader("Authorization", "");
                response.sendRedirect("http://localhost:8051");
            }
        }

        return "hello alipay!";
    }
}
*/
