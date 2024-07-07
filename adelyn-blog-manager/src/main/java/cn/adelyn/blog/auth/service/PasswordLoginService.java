package cn.adelyn.blog.auth.service;

import cn.adelyn.blog.auth.pojo.dto.AuthenticationDTO;
import cn.adelyn.blog.auth.pojo.vo.TokenInfoVO;
import cn.adelyn.framework.core.pojo.bo.UserInfoBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordLoginService extends AbstractLoginService{

    @Autowired
    private AuthAccountService authAccountService;

    public String getAuthCode(AuthenticationDTO authenticationDTO) {
        UserInfoBO userInfoBO = authAccountService.verifyPassword(authenticationDTO.getUserName(),
                        authenticationDTO.getPassword());

        return getAuthCode(userInfoBO);
    }
}
