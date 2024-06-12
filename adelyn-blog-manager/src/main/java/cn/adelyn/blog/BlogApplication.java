package cn.adelyn.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author chengzelee
 * @Date 2023/1/22 18:06
 * @Desc search 启动类
 */
@SpringBootApplication(scanBasePackages = { "cn.adelyn", "cn.adelyn.framework.core" })
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
