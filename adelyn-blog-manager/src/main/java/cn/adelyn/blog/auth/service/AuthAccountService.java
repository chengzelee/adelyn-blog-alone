package cn.adelyn.blog.auth.service;

import cn.adelyn.blog.auth.constant.AuthAccountStatus;
import cn.adelyn.blog.auth.dao.po.AuthAccountPO;
import cn.adelyn.blog.auth.dao.service.AuthAccountDaoService;
import cn.adelyn.blog.auth.pojo.dto.RegisterAccountDTO;
import cn.adelyn.blog.manager.service.SnowflakeService;
import cn.adelyn.framework.core.cglib.BeanCopierUtil;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.pojo.bo.UserInfoBO;
import cn.adelyn.framework.core.response.ServerResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author chengze
 * @date 2022/12/20
 * @desc 账号认证
 */
@Service
@AllArgsConstructor
public class AuthAccountService {

	private final AuthAccountDaoService authAccountDaoService;
	private final SnowflakeService snowflakeRpcService;
	private final PasswordEncoder passwordEncoder;

	public static final String USER_NOT_FOUND_SECRET = "$2a$10$qgsD1Ez6oBYiE61dLAAkBO9dKzrv5oho3J9G0nR/eIDs75oY4hCvq";

	public UserInfoBO verifyPassword(String userName, String password) {

		AuthAccountPO authAccountPO = authAccountDaoService.getAuthAccountByUserName(userName);

		if (Objects.isNull(authAccountPO)) {
			// 防止计时攻击（Timing-attack）
			// 通过设备运算的用时来推断出所使用的运算操作，或者通过对比运算的时间推定数据位于哪个存储设备，或者利用通信的时间差进行数据窃取。
			mitigateAgainstTimingAttack(password);
			throw new AdelynException("userInfo inValid");
		}

		if (!Objects.equals(authAccountPO.getStatus(), AuthAccountStatus.enable)) {
			throw new AdelynException("userInfo inValid");
		}

		if (!passwordEncoder.matches(password, authAccountPO.getPassword())) {
			throw new AdelynException("userInfo inValid");
		}

		return BeanCopierUtil.copy(authAccountPO, UserInfoBO.class);
	}

	public ServerResponse registerAccount(RegisterAccountDTO registerAccountDTO){

		String name = registerAccountDTO.getUserName();
		String password = passwordEncoder.encode(registerAccountDTO.getPassword());

		AuthAccountPO authAccountPO = new AuthAccountPO();
		authAccountPO.setUserName(name);
		authAccountPO.setPassword(password);
		authAccountPO.setUserId(snowflakeRpcService.nextId());
		authAccountPO.setStatus(AuthAccountStatus.enable);

		authAccountDaoService.insetAccount(authAccountPO);

		return ServerResponse.success();
	}

	/**
	 * 防止计时攻击
	 */
	private void mitigateAgainstTimingAttack(String presentedPassword) {
		this.passwordEncoder.matches(presentedPassword, USER_NOT_FOUND_SECRET);
	}

}
