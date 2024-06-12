package cn.adelyn.blog.test;

import cn.adelyn.blog.auth.pojo.dto.AuthenticationDTO;
import cn.adelyn.blog.auth.pojo.dto.RegisterAccountDTO;
import cn.adelyn.blog.auth.pojo.vo.TokenInfoVO;
import cn.adelyn.blog.auth.service.AuthAccountService;
import cn.adelyn.blog.auth.service.LoginService;
import cn.adelyn.framework.crypto.constant.AlgoConstant;
import cn.adelyn.framework.crypto.utils.JwtUtil;
import cn.adelyn.framework.crypto.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;

@Slf4j
@SpringBootTest
public class AuthTester {

    @Autowired
    private AuthAccountService authAccountService;

    @Autowired
    private LoginService loginService;

    private final String USER_NAME = "lichengze";
    private final String PASSWORD = "123456";

    @Test
    void resistAccount() {
        RegisterAccountDTO registerAccountDTO = new RegisterAccountDTO();
        registerAccountDTO.setUserName(USER_NAME);
        registerAccountDTO.setPassword(PASSWORD);

        authAccountService.registerAccount(registerAccountDTO);
    }

    @Test
    void login() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUserName(USER_NAME);
        authenticationDTO.setPassword(PASSWORD);

        TokenInfoVO tokenInfoVO = loginService.login(authenticationDTO);

        log.info("token: {}", tokenInfoVO);
    }

    @Test
    void jwtTest() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        KeyPair keyPair = KeyUtil.generateKeyPair(AlgoConstant.RSA);

        String base64privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String base64PublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        PrivateKey privateKey = KeyUtil.getPrivateKey(Base64.getDecoder().decode(base64privateKey));
        PublicKey publicKey = KeyUtil.getPublicKey(Base64.getDecoder().decode(base64PublicKey));

        Date currentDate = new Date();
        // 最少一秒，再少了解析不到
        String token = JwtUtil.generateToken("123", currentDate, 1000, privateKey);
        String sub = JwtUtil.validateToken(token, new Date(currentDate.getTime() + 1), publicKey);

        log.info("publicKey: {}", base64PublicKey);
        log.info("privateKey: {}", base64privateKey);
        log.info("jwt: {}", token);
        log.info("sub: {}", sub);
    }
}
