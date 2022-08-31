package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.demo.config.StaticConfig.PIC_DIR;
import static com.example.demo.config.StaticConfig.PIC_PATTERN;


/**
 * 注册拦截器
 *
 * @author zhangduhuang
 */
@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurationSupport {

    @Autowired
    private CroInterceptor croInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){ //设置跨域
        registry.addInterceptor(croInterceptor).addPathPatterns("/**");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { //静态资源转换器，但是这里还有类似静态资源缓存器
        registry.addResourceHandler("/image1/**").addResourceLocations("file:E://aaa/"); //设置图片拦截
        super.addResourceHandlers(registry);
    }
}
