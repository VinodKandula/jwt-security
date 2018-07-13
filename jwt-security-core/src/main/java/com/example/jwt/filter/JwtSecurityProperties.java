package com.example.jwt.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public class JwtSecurityProperties {

    private Token token = new Token();

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public static class Token {

        private String secret;

        private long validity = 1800;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getValidity() {
            return validity;
        }

        public void setValidity(long validity) {
            this.validity = validity;
        }
    }
}
