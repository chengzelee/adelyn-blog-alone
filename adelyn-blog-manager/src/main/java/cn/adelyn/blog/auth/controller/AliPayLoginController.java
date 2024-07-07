package cn.adelyn.blog.auth.controller;

import cn.adelyn.blog.auth.service.AliPayLoginService;
import cn.adelyn.framework.core.response.ServerResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth/login/aliPay")
public class AliPayLoginController {

    private final AliPayLoginService aliPayLoginService;

    @GetMapping("/public")
    public ServerResponse<String> login() {
        return ServerResponse.success(aliPayLoginService.getAliPayRedirectUrl());
    }

    @GetMapping("/public/callBack")
    public void callBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 防csrf
        String state = request.getParameter("state");
        String aliPayAuthCode = request.getParameter("auth_code");
        log.info("aliPay authCode: {}", aliPayAuthCode);

        String authCode = aliPayLoginService.getAuthCode(state, aliPayAuthCode);

        if (StringUtils.hasLength(authCode)) {
            response.sendRedirect(aliPayLoginService.getRedirectUrl(authCode));
        }
    }
}
