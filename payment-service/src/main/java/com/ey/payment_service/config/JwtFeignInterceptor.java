package com.ey.payment_service.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@RequiredArgsConstructor
@Configuration
@Slf4j
public class JwtFeignInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {

            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attrs == null) {
                return;
            }
                HttpServletRequest request = attrs.getRequest();
                String authHeader = request.getHeader("Authorization");

                log.info("feign forwarding token: {}", authHeader);

                if (authHeader != null && authHeader.startsWith("Bearer")) {
                    requestTemplate.header("Authorization", authHeader);
                }
        };
    }
}