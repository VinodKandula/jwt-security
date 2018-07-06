package com.example.jwtsecurityautoconfigure;

import com.example.jwt.filter.JwtHelper;
import com.example.jwt.filter.JwtSecurityProperties;
import com.example.jwt.filter.JwtTokenFilter;
import com.example.jwt.rest.JwtRestTemplateCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtSecurityProperties.class)
public class JwtSecurityAutoConfiguration {

    private final JwtSecurityProperties jwtSecurityProperties;

    public JwtSecurityAutoConfiguration(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    @Bean
    public JwtHelper jwtHelper() {
        return new JwtHelper(jwtSecurityProperties);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(JwtHelper jwtHelper) {
        return new JwtTokenFilter(jwtHelper);
    }

    @Bean
    public JwtRestTemplateCustomizer jwtRestTemplateCustomizer() {
        return new JwtRestTemplateCustomizer();
    }

}
