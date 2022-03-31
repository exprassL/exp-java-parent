package com.exp.web.sso.baidu.util;

import com.exp.fluent.entity.User;
import com.exp.redis.RedisUtil;
import com.exp.shiro.ShiroUtil;
import com.exp.web.sso.baidu.response.TokenResponse;

/**
 * 百度数据或 Api 相关 redis 缓存操作
 */
public final class BaiduRedisUtil {
    
    /**
     * 百度 Access_token 缓存 key
     */
    private static final String ACCESS_TOKEN_CACHE_KEY = "BaiduToken";
    
    /**
     * 当前用户的 Access_token 、 refresh_token 等信息入缓存
     *
     * @param tokenResponse 包含 Access_token 和 refresh_token 信息
     */
    public static void cacheAccessToken(TokenResponse tokenResponse) {
        String hashKey = ShiroUtil.getCurrentUser().getId().toString();
        RedisUtil.hashSet(ACCESS_TOKEN_CACHE_KEY, hashKey, tokenResponse);
    }
    
    /**
     * 从缓存取出当前用户的 Access_token 、 refresh_token 等信息
     *
     * @return 包含 Access_token 和 refresh_token 信息
     */
    public static TokenResponse getAccessToken() {
        String hashKey = ShiroUtil.getCurrentUser().getId().toString();
        return (TokenResponse) RedisUtil.hashGet(ACCESS_TOKEN_CACHE_KEY, hashKey);
    }
}
