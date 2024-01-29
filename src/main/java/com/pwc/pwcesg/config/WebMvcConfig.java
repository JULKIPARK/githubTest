package com.pwc.pwcesg.config;

import com.pwc.pwcesg.backoffice.common.service.BoLoginService;
import com.pwc.pwcesg.backoffice.intro.beans.BackofficeInterceptor;
import com.pwc.pwcesg.frontoffice.common.service.FoCommonService;
import com.pwc.pwcesg.frontoffice.intro.beans.FrontofficeInterceptor;
import com.pwc.pwcesg.frontoffice.intro.beans.IntroInterceptor;
import com.pwc.pwcesg.frontoffice.intro.service.IntroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${fileInfo.connectPath}")
    private String connectPath;
    @Value("${fileInfo.resourcePath}")
    private String resourcePath;

    private final IntroService introService;
    private final BoLoginService boLoginService;
    private final FoCommonService foCommonService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new IntroInterceptor(introService)).order(1)
                // 해당 경로에 접근하기 전에 인터셉터가 가로챈다.
                .addPathPatterns("/**")
                // 해당 경로는 인터셉터가 가로채지 않는다.
                .excludePathPatterns("/error")
                .excludePathPatterns("/assets/**")
                .excludePathPatterns("/sems/assets/**")
//                .excludePathPatterns("/frontoffice/img/**")
//                .excludePathPatterns("/frontoffice/js/**")
//                .excludePathPatterns("/frontoffice/css/**")
//                .excludePathPatterns("/frontoffice/fonts/**")
//                .excludePathPatterns("/frontoffice/html/**")
        ;

        registry.addInterceptor(new BackofficeInterceptor(boLoginService))
                // 해당 경로에 접근하기 전에 인터셉터가 가로챈다.
                .addPathPatterns("/sems/**")
                // 해당 경로는 인터셉터가 가로채지 않는다.
                .excludePathPatterns("/error")
                .excludePathPatterns("/sems/assets/**")
                .excludePathPatterns("/sems/common/login")
                .excludePathPatterns("/sems/common/signUp")
                .excludePathPatterns("/sems/common/findMe")
                .excludePathPatterns("/sems/common/selectMyInfo")
                .excludePathPatterns("/sems/common/certificate")
//                .excludePathPatterns("/sems/js/**")
//                .excludePathPatterns("/sems/css/**")
//                .excludePathPatterns("/sems/fonts/**")
//                .excludePathPatterns("/sems/html/**")
        ;

        registry.addInterceptor(new FrontofficeInterceptor(boLoginService))
        // 해당 경로에 접근하기 전에 인터셉터가 가로챈다.
//                .addPathPatterns("/**")
        // 해당 경로는 인터셉터가 가로채지 않는다.
                .excludePathPatterns("/error")
//                .excludePathPatterns("/frontoffice")
                .excludePathPatterns("/common/login")
                .excludePathPatterns("/common/signUp")
                .excludePathPatterns("/assets/**")
//                .excludePathPatterns("/frontoffice/js/**")
//                .excludePathPatterns("/frontoffice/css/**")
//                .excludePathPatterns("/frontoffice/fonts/**")
//                .excludePathPatterns("/frontoffice/html/**")
        ;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler(connectPath).addResourceLocations(resourcePath);

        registry.addResourceHandler("/assets/**") //(2)
                .addResourceLocations("classpath:/static/frontoffice/");

        registry.addResourceHandler("/sems/assets/**") //(2)
                .addResourceLocations("classpath:/static/backoffice/");

    }
}
