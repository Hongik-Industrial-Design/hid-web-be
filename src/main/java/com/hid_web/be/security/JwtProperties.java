package com.hid_web.be.security;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JwtProperties {
    private String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"; // 은닉 필요
    private long expiration = 86400000;
    private String tokenPrefix = "Bearer ";
    private String headerName = "Authorization";
}
