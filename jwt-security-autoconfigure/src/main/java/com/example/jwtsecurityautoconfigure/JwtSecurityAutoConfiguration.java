package com.example.jwtsecurityautoconfigure;

import com.example.jwt.filter.JwtSecurityProperties;
import com.example.jwt.rest.JwtRestTemplateCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtSecurityProperties.class)
public class JwtSecurityAutoConfiguration {

    @Bean
    public JwtRestTemplateCustomizer jwtRestTemplateCustomizer() {
        return new JwtRestTemplateCustomizer();
    }

}
