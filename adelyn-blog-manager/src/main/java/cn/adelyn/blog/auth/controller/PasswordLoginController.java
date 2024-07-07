package cn.adelyn.blog.auth.controller;

import cn.adelyn.blog.auth.pojo.dto.AuthenticationDTO;
import cn.adelyn.blog.auth.pojo.vo.TokenInfoVO;
import cn.adelyn.blog.auth.service.PasswordLoginService;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.response.ServerResponse;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author chengze
 * @date 2022/12/20
 * @desc 登录接口
 */
@RestController
@AllArgsConstructor
@RequestMapping("/auth/login/password")
public class PasswordLoginController {

	private final PasswordLoginService loginService;

/*	@PostMapping("/public")
	public ServerResponse<String> login(@Valid @RequestBody AuthenticationDTO authenticationDTO) throws IOException {
		try(Entry entry = SphU.entry("login")) {
			String authCode = loginService.getAuthCode(authenticationDTO);
			return ServerResponse.success(loginService.getRedirectUrl(authCode));
		} catch (BlockException e) {
			throw new AdelynException(ResponseEnum.TOO_MANY_REQUEST);
		}
	}*/

}
