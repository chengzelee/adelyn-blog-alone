package cn.adelyn.blog.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Configuration
public class AntPathMatcherConfig {

    @Bean
    public AntPathMatcher initAntPathMatcher() {
        return new AntPathMatcher();
    }
}
