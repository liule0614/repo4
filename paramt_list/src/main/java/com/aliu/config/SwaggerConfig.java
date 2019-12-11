package com.aliu.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.enable() 是否启动swagger
                .select()
                //RequestHandlerSelectors 扫描接口方式
                //basePackage 扫描包
                .apis(RequestHandlerSelectors.basePackage("com.aliu.controller"))
                //.paths 过滤路径
                .build();
    }

    private ApiInfo apiInfo(){
        Contact contact = new Contact("刘晓北","baidu.com","835707769@qq.com");
        return new ApiInfo("三组的Api文档",
                "让我们荡起双桨",
                "1.0",
                "https://www.bilibili.com/video/av64841843?p=2",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0"
        );
    }

}
