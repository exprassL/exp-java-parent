package com.exp.web.sso.baidu.util;

import com.exp.fluent.constant.TokenType;
import com.exp.fluent.entity.User;
import com.exp.shiro.ShiroUtil;
import com.exp.web.exception.WebBizException;
import com.exp.web.sso.baidu.response.TokenResponse;
import com.exp.web.sso.baidu.response.UserInfoResponse;
import org.springframework.web.client.RestTemplate;

/**
 * 封装对百度 Api 的调用操作
 */
public final class BaiduApiUtil {
    
    /**
     * code 换取 Access_token
     *
     * @param code        code ，用户换取 Access_token
     * @param appKey      注册的百度（网盘）应用的相关参数
     * @param secretKey   同上
     * @param redirectUri 同上
     * @return 包含 Access_token 和 refresh_token 信息的 Api 回参
     */
    public static TokenResponse oauth(String code, String appKey, String secretKey, String redirectUri) {
        String oauthUrl = "https://openapi.baidu.com/oauth/2.0/token?" +
                "grant_type={grantType}&" +
                "code={code}&" +
                "client_id={clientId}&" +
                "client_secret={clientSecret}&" +
                "redirect_uri={redirectUri}";
        
        RestTemplate template = new RestTemplate();
        TokenResponse tokenResponse = template.getForObject(oauthUrl, TokenResponse.class, "authorization_code", code, appKey, secretKey, redirectUri);
        if (tokenResponse == null) {
            throw WebBizException.produce("OAuth 接口[%s]响应为空", oauthUrl);
        }
        return tokenResponse;
    }
    
    /**
     * 刷新 Access_Token
     */
    public static void refreshToken(String refreshToken, String appKey, String secretKey) {
        String refreshUri = "https://openapi.baidu.com/oauth/2.0/token?" +
                "grant_type=refresh_token&" +
                "refresh_token=" + refreshToken +
                "&client_id=" + appKey +
                "&client_secret=" + secretKey;
        RestTemplate template = new RestTemplate();
        TokenResponse tokenResponse = template.getForObject(refreshUri, TokenResponse.class);
        // 可能发生异常： expired_token	refresh token has been used	提供的Refresh Token已过期
        BaiduRedisUtil.cacheAccessToken(tokenResponse);
    }
    
    /**
     * 获取百度(网盘）用户信息
     *
     * @param accessToken 授权凭据
     * @return  百度（网盘）用户信息
     */
    public static UserInfoResponse userInfo (String accessToken) {
        String uInfoUrl = "https://pan.baidu.com/rest/2.0/xpan/nas?method={method}&access_token={accessToken}";
        return RestfulUtil.doGet(uInfoUrl, UserInfoResponse.class, "uinfo", accessToken);
    }
    
    public static void foo() {
        User currentUser = ShiroUtil.getCurrentUser();
        TokenType tokenType = currentUser.getTokenType();
        if (tokenType == TokenType.BAIDU) {
            // 百度（网盘）帐号，可以获取百度的 Access_token 并访问百度 Api
            TokenResponse tokenResponse = BaiduRedisUtil.getAccessToken();
            String accessToken = tokenResponse.getAccessToken();
            // 使用此 Access_token 调用百度 api ，如果报 token 过期，需要刷新 AccessToken
            // 111	Access token expired	token已过期
        
        } else {
            // 非百度网盘账号， fallback 到管理员的百度网盘帐号
            // 使用此 Access_token 调用百度 api ，如果报 token 过期，需要刷新 AccessToken
            
        }
    }
}
