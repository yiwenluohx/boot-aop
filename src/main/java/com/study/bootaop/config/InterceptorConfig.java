package com.study.bootaop.config;

import com.study.bootaop.interceptor.AuthorizeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: InterceptorConfig
 * Description: 拦截器配置生效
 * @Author: luohx
 * Date: 2022/4/18 下午2:19
 * History:
 * <author>          <time>          <version>          <desc>
 * luohx            修改时间           1.0           拦截器配置生效
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor getAuthorizeInterceptor() {
        return new AuthorizeInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthorizeInterceptor())
                .excludePathPatterns("/integration/**")
                .addPathPatterns("/**");
    }
}
