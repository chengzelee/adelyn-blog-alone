/*
package cn.adelyn.blog.auth.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliPayConfig {

    // 支付宝网关
    @Value("${adelyn.blog.auth.aliPay.gateWay:https://openapi.alipay.com/gateway.do}")
    private String ALIPAY_GATEWAY;

    */
/**appID**//*

    @Value("${adelyn.blog.auth.aliPay.appId:appId}")
    private String APP_ID;
    */
/**私钥*//*

    @Value("${adelyn.blog.auth.aliPay.privateKey:privateKey}")
    private String APP_PRIVATE_KEY;
    */
/**支付宝公钥*//*

    @Value("${adelyn.blog.auth.aliPay.publicKey:publicKey}")
    private String ALIPAY_PUBLIC_KEY;


    @Bean
    public AlipayClient initClient() {
        return new DefaultAlipayClient(ALIPAY_GATEWAY, APP_ID, APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");
    }
}
*/
