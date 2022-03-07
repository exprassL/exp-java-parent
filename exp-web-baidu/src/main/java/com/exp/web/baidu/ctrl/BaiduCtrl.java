package com.exp.web.baidu.ctrl;

import com.exp.model.response.MapResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 百度开放能力接入ctrl
 */
@RestController
@RequestMapping(value = "/baidu/*")
public class BaiduCtrl {
    
    /*
     * 以下放到同一的配置里：
     * baidu.redirect.uri
     * baidu.app.net-disk.growing-story.app-key
     * baidu.app.net-disk.growing-story.secret-key
     */
    @Value("${baidu.redirect.uri:这里时默认值}")
    private String redirectUri;
    private final String appKey = "xV508jGyGYHDTMdG3rCZLztWTF93lcRu";
    private final String secretKey = "rybByzXGVs155EptAfCosfBFrnD3fNoP";
    
    /**
     * 获取用户授权后，换取access_token
     * @param code
     * @return
     */
    @GetMapping(value = "/redirect")
    public MapResult oauthWithCode(String code) {
        String oauthUrl = "https://openapi.baidu.com/oauth/2.0/token?" +
                "grant_type=authorization_code&" +
                "code=" + code +
                "&client_id=" + appKey +
                "&client_secret=" + secretKey +
                "&redirect_uri=" + redirectUri;
        
        RestTemplate template = new RestTemplate();
        
        // 实际这些参数不应该返回给前台，而是应该放在后端使用。
        return new MapResult().put("access_token", "").put("refresh_token", "").put("scope", "").put("expires_in", "");
    }
    
    /**
     * 刷新access_token
     * @return
     */
    @GetMapping(value = "/refreshToken")
    public MapResult refreshToken() {
        String refreshToken = ""; // 上一接口返回的
        String refreshUri = "https://openapi.baidu.com/oauth/2.0/token?" +
                "grant_type=refresh_token&" +
                "refresh_token=" + refreshToken +
                "&client_id=" + appKey +
                "&client_secret=" + secretKey;
        RestTemplate template = new RestTemplate();
//        template.getFor
        
        // 实际这些参数不应该返回给前台，而是应该放在后端使用。
        return new MapResult().put("access_token", "").put("refresh_token", "").put("scope", "").put("expires_in", "");
    }
}
