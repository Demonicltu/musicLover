package com.music.lover.hometask.configuration;

import com.music.lover.hometask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "com.music.lover.hometask")
public class AppConfig implements WebMvcConfigurer {

    private final UserService userService;

    @Autowired
    public AppConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor(userService))
                .addPathPatterns("/v1/**")
                .excludePathPatterns("/v1/users/**");
    }

}
