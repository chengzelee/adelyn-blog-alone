package cn.adelyn.blog.auth.controller;

import cn.adelyn.blog.auth.pojo.dto.AuthenticationDTO;
import cn.adelyn.blog.auth.pojo.vo.TokenInfoVO;
import cn.adelyn.blog.auth.service.LoginService;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.response.ServerResponse;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chengze
 * @date 2022/12/20
 * @desc 登录接口
 */
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class LoginController {

	private final LoginService loginService;

	@PostMapping("/public/login")
	public ServerResponse<TokenInfoVO> login(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
		try(Entry entry = SphU.entry("login")) {
			return ServerResponse.success(loginService.login(authenticationDTO));
		} catch (BlockException e) {
			throw new AdelynException(ResponseEnum.TOO_MANY_REQUEST);
		}
	}

}
