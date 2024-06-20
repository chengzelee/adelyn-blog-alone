package cn.adelyn.blog.auth.service;

import cn.adelyn.blog.auth.pojo.vo.TokenInfoVO;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.pojo.bo.UserInfoBO;
import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.util.StringUtil;
import cn.adelyn.framework.crypto.utils.JwtUtil;
import cn.adelyn.framework.crypto.utils.KeyUtil;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    @Value("${adelyn.auth.token.privateKey:MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCehBD4lryokSdokk9XkXldro2hjbKSJZX1DdZNQg0haAXf6RNxM7g/4w76SN+9buMOwsQU/APP6sndRGFVOdTd111XFANrQy4+zE1VpYEQtSdYbBiqF2GWhuW11v+im3FBRsEq2xDO4NbyMPqJL8OijBz0WT3Kjgz65XIiKIa7Co532NOgAGJ3mPAj0/NqQs9B/go7ru2slmQQOmSL1cZJDXNuJLpmViZZ/mTUBslMdYZjJSLhpArRvMe4/VDvhniB3EiDaX8gTaR0VnQfZg0ahtIl6VRCxVwil6TXBdAFVGjMhQswFwxTAQnLihSENjF7EfSzvkrAu6JxUKPgrqlTAgMBAAECggEACD9Yf16fD+mG0owzlZug5BdrZg7weLsnkLmjafSi/ex4IHIeNq3ECzWTTMFoPBEeE4wIVgvWCkYt3OssYe9MMdin52/H98I7Oz2M/oT0cKaZlqJVLDBi6Ek6KsULcENEgsAdgnnQZ5B6OTKu2QluVTGTZhaTPkvixVZqeE2B07nADmTFYU2FnN4IW3D7FHEv3PwlOD73bA+toYexej109ECooGLDkcSPOo6wD4rOeFfNLi3+Emw1go9lhYr4yZwIl1ukjhOphrdMRDx6lLmadbEfd5IZVMvitf8+oWxGQhzsW0BhQKhCzoAUXoRqqwS+10bnM3tIujh15SE3va4EhQKBgQDU1zByHjbn5Oek5xf2GmFVKAPVawuLRVY0mDUCxl1BGQPT4ZBd2CPMGknjKMKLOslCmpXTGkTbocIj2mxrXE98MVimZNz3Wy6yGtrAZGM1+A1VMG2NNQ9Ro4A2bC+vGN38ZQMSK3SZMsIZa+UJno+lswOdeHK0XFW6kmGdMS9nvQKBgQC+qNEKH9TYxyXfINr7cN6dFkKqTl2c3HageBWSrTA04Ij8525QOlneSbi9UBuplJ/TRPynpneNRrWZxq3zqIzSMSO0NXsuCYES18G80xWe6tlMqElyH9Nu3uSh1R1zXd2BpoeUAwHDPQ8WUViZrF+SP7kRRL5aGuIzFCRtlhOeTwKBgGZg7Ci4Z+i7rsEfd+MlZVQ030M1LmRbgc/itv0StaCui6zLf0nkwbNXodrmrE2UhWLIQPE+XYjYEi50zloGGSXABshjE/bXoWqCiF6q6x32p1Mm7EtUDn5Si0WiXH15FK5nWEbRKFDYnYzk3VL3NlmZ9k80tNgU4hfQeavHEOpxAoGAJQ6m4rLhKek7QS0fovODMUEcYAGvTEfFftcYSlQDJKPnvFA3LrzIrt3hXFpvpfxPPD8SZHuEjR8LOQJW2R8Pj+MhL2udr4sFXWY2L79PWn4HElE7RUVOUSRdh97mRIAgB40YmPoko5AAmLPi5quMP97G18ZmwRiskoT9t5cN5R0CgYANMVcFoZA4dektc+O1ozD3Y3PCfXXl8KG4OBRulqRSKnlpwsz8Xo+GHEWk9y4VNNJ3g3V8SQf/CsukUbK8RtwM27HiPpboPz0ClyYCgcNLURVbe7S7os9/QwXMKVsMAoth6pgXDUWm/ZYQeiziI+VmisITEk36rOxbCbMPTZzQFQ==}")
    private String base64PrivateKey;

    @Value("${adelyn.auth.token.publicKey:MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnoQQ+Ja8qJEnaJJPV5F5Xa6NoY2ykiWV9Q3WTUINIWgF3+kTcTO4P+MO+kjfvW7jDsLEFPwDz+rJ3URhVTnU3dddVxQDa0MuPsxNVaWBELUnWGwYqhdhlobltdb/optxQUbBKtsQzuDW8jD6iS/Doowc9Fk9yo4M+uVyIiiGuwqOd9jToABid5jwI9PzakLPQf4KO67trJZkEDpki9XGSQ1zbiS6ZlYmWf5k1AbJTHWGYyUi4aQK0bzHuP1Q74Z4gdxIg2l/IE2kdFZ0H2YNGobSJelUQsVcIpek1wXQBVRozIULMBcMUwEJy4oUhDYxexH0s75KwLuicVCj4K6pUwIDAQAB}")
    private String base64PublicKey;

    @Value("${adelyn.auth.token.access.expirationTime:60000}")
    private long accessTokenExpirationTime;

    @Value("${adelyn.auth.token.refresh.expirationTime:120000}")
    private long refreshTokenExpirationTime;

    private final Map<String, PrivateKey> keyMap = new ConcurrentHashMap<>();
    private final Map<String, PublicKey> publicKeyMap = new ConcurrentHashMap<>();
    private final String PUBLIC_KEY = "publicKey";
    private final String PRIVATE_KEY = "privateKey";

    public TokenInfoVO generateTokenPair(UserInfoBO userInfoBO) {
        String accessToken = generateAccessToken(userInfoBO);
        String refreshToken = generateRefreshToken(userInfoBO);

        return TokenInfoVO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenInfoVO refreshToken(String refreshToken) {
        UserInfoBO userInfoBO = validToken(refreshToken);

        return generateTokenPair(userInfoBO);
    }

    public String generateAccessToken(UserInfoBO userInfoBO) {
        return generateToken(userInfoBO, accessTokenExpirationTime);
    }

    public String generateRefreshToken(UserInfoBO userInfoBO) {
        return generateToken(userInfoBO, refreshTokenExpirationTime);
    }

    private String generateToken(UserInfoBO userInfoBO, long expirationTime) {
        PrivateKey privateKey = keyMap.computeIfAbsent(PRIVATE_KEY, value -> {
            try {
                return KeyUtil.getPrivateKey(Base64.getDecoder().decode(base64PrivateKey));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
                throw new AdelynException("get token privateKey err", e);
            }
        });

        return generateToken(JSON.toJSONString(userInfoBO), expirationTime, privateKey);
    }

    public UserInfoBO validToken(String token) {
        PublicKey publicKey = publicKeyMap.computeIfAbsent(PUBLIC_KEY, value -> {
            try {
                return KeyUtil.getPublicKey(Base64.getDecoder().decode(base64PublicKey));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
                throw new AdelynException("get token privateKey err", e);
            }
        });

        String jsonStr;

        try {
            jsonStr = validateToken(token, publicKey);
        } catch (Exception e) {
            throw new AdelynException(ResponseEnum.UNLOGIN);
        }

        if (!StringUtil.hasLength(jsonStr)) {
            throw new AdelynException(ResponseEnum.UNLOGIN);
        }

        return JSON.parseObject(jsonStr, UserInfoBO.class);
    }

    private String validateToken(String token, PublicKey publicKey) {
        return JwtUtil.validateToken(token, new Date(), publicKey);
    }

    private String generateToken(String subject, long expirationTime, PrivateKey privateKey) {
        return JwtUtil.generateToken(subject, new Date(), expirationTime, privateKey);
    }
}
