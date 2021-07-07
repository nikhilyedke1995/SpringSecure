package com.demo.secure.jwt;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@ConfigurationProperties(prefix = "application.jwt")
@Component
public class JwtConfig {

    private String secretKey,tokenPrefix;
    private int tokenExpirationAfterDays;

    public String getSecretKey() {
        return secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public int getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public void setTokenExpirationAfterDays(int tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }

    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }
}
