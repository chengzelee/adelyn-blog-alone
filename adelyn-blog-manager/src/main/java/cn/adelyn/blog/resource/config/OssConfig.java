package cn.adelyn.blog.resource.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.Protocol;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {

    @Value("${adelyn.blog.oss.endpoint:https://oss-cn-shanghai.aliyuncs.com}")
    private String endpoint;
    @Value("${adelyn.blog.oss.bucketName:adelyn-blog-test}")
    private String ossBucketName;
    @Value("${adelyn.blog.oss.accessKeyId:ak}")
    private String accessKeyId;
    @Value("${adelyn.blog.oss.accessKeySecret:sk}")
    private String accessKeySecret;

    @Getter
    @Value("${adelyn.blog.oss.domain:https://oss-test.adelyn.cn}")
    private String adelynOssDomain;


    public String getBucketName() {
        return ossBucketName;
    }

    // todo 用的时候再写
    public String getCallBackUrl() {
        return "tenant call back url";
    }

    @Bean
    public OSS initOssClient() {
        // 创建ClientBuilderConfiguration。
        // ClientBuilderConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置连接OSS所使用的协议（HTTP或HTTPS），默认为HTTP。
        conf.setProtocol(Protocol.HTTPS);
        // 使用代码嵌入的RAM用户的访问密钥配置访问凭证。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        // 创建OSSClient实例。
        return new OSSClientBuilder().build(endpoint, credentialsProvider, conf);
    }
}
