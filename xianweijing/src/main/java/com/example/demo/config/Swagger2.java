package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @author zhangduhuang
 * @date 2021/7/17 23:35
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    @Value("${my.address}")
    private String ip;
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("com.example.demo.controller")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("小肠液相关指标检测数据收集接口文档").description("作为前后端交接文档,具体在线文档请访问： http://"+ip+"/v2/api-docs").termsOfServiceUrl("http://github.com/").contact("xiaozhang").version("1.0").build();
    }
}
