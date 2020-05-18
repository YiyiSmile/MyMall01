package indi.tom.mymall01.config;

import indi.tom.mymall01.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author Tom
 * @Date 2020/5/18 18:24
 * @Version 1.0
 * @Description
 */
@Configuration
public class MyMVCConfig extends WebMvcConfigurerAdapter {

    @Autowired
    AuthInterceptor authInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }
}
