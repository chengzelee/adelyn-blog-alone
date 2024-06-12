package cn.adelyn.blog.auth.controller;

import cn.adelyn.blog.auth.pojo.dto.RefreshTokenDTO;
import cn.adelyn.blog.auth.pojo.vo.TokenInfoVO;
import cn.adelyn.blog.auth.service.TokenService;
import cn.adelyn.framework.core.response.ServerResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class TokenController {

    private TokenService tokenService;

    @PostMapping("/token/refresh")
    public ServerResponse<TokenInfoVO> login(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        return ServerResponse.success(tokenService.refreshToken(refreshTokenDTO.getRefreshToken()));
    }
}
