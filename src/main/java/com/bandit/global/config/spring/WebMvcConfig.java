package com.bandit.global.config.spring;

import com.bandit.domain.member.service.MemberQueryService;
import com.bandit.global.annotation.auth.CustomAuthenticationPrincipalArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MemberQueryService memberQueryService;
    private final long MAX_AGE_SECS = 3600;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CustomAuthenticationPrincipalArgumentResolver(memberQueryService));
    }

    //CORS setting
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") //CORS 적용할 URL 패턴
                .allowedOriginPatterns("*") //자원 공유 오리진 지정
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") //요청 허용 메서드
                .allowedHeaders("*") //요청 허용 헤더
                .allowCredentials(true) //요청 허용 쿠키
                .maxAge(MAX_AGE_SECS);
    }
}
