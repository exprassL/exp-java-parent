package com.exp.web.sso.baidu.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * 获取、刷新 Access_token 接口的回参
 */
@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenResponse {
    
    private String accessToken;
    
    /**
     * Access_token 有效期
     */
    private int expiresIn;
    
    private String refreshToken;
    
    /**
     * Access_token 的授权列表
     */
    private String scope;
    
}
