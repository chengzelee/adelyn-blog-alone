package cn.adelyn.blog.auth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AliPayConfig {

    // 支付宝网关
    @Value("${adelyn.blog.auth.aliPay.gateWay:https://openapi.alipay.com/gateway.do}")
    private String aliPayGateway;

    /**appID**/
    @Value("${adelyn.blog.auth.aliPay.appId:appId}")
    private String appId;

    @Value("${adelyn.blog.auth.aliPay.redirectUrl:https%3a%2f%2fblog.adelyn.cn%2fblog-backend%2fauth%2flogin%2faliPay%2fpublic%2fcallBack}")
    private String redirectUrl;

    /**私钥*/
    @Value("${adelyn.blog.auth.aliPay.privateKey:privateKey}")
    private String appPrivateKey;

    @Value("${adelyn.blog.auth.aliPay.publicKey:publicKey}")
    private String aliPayPublicKey;
}
