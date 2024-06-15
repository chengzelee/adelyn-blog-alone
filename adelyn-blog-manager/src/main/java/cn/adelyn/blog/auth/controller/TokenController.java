package cn.adelyn.blog.auth.controller;

import cn.adelyn.blog.auth.pojo.dto.RefreshTokenDTO;
import cn.adelyn.blog.auth.pojo.vo.TokenInfoVO;
import cn.adelyn.blog.auth.service.TokenService;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.response.ServerResponse;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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
    public ServerResponse<TokenInfoVO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        try(Entry entry = SphU.entry("token_refresh")) {
            return ServerResponse.success(tokenService.refreshToken(refreshTokenDTO.getRefreshToken()));
        } catch (BlockException e) {
            throw new AdelynException(ResponseEnum.TOO_MANY_REQUEST);
        }
    }
}
