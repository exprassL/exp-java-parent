package com.exp.web.sso.baidu.ctrl;

import com.exp.fluent.constant.TokenType;
import com.exp.fluent.entity.User;
import com.exp.service.impl.UserService;
import com.exp.shiro.ExpAuthorizingRealm;
import com.exp.web.sso.baidu.response.TokenResponse;
import com.exp.web.sso.baidu.response.UserInfoResponse;
import com.exp.web.sso.baidu.util.BaiduApiUtil;
import com.exp.web.sso.baidu.util.BaiduRedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 百度开放能力接入ctrl
 */
@RestController
@RequestMapping(value = "/baidu/*")
public class BaiduCtrl {
    
    @Value("${app.growing-story.home:http://127.0.0.1:8080/}")
    private String appHome;
    @Value("${app.growing-story.baidu.net-disk.app-key}")
    private String appKey;
    @Value("${app.growing-story.baidu.net-disk.secret-key}")
    private String secretKey;
    @Value("${app.growing-story.baidu.net-disk.redirect.uri:http://127.0.0.1:8080/sso/baidu/redirect}")
    private String redirectUri;
    
    @Resource
    private UserService userService;
    
    /**
     * 授权登录引导页
     *
     * @param response 响应，用于重定向
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping(value = "/guide")
    public void guide(HttpServletResponse response) throws IOException {
        String authorizeUrlPattern = "http://openapi.baidu.com/oauth/2.0/authorize?response_type=code&client_id=xV508jGyGYHDTMdG3rCZLztWTF93lcRu&redirect_uri=%s&scope=basic,netdisk&display=popup&qrcode=1&force_login=0&device_id=820921428tp8x63q51";
        response.sendRedirect(String.format(authorizeUrlPattern, redirectUri));
    }
    
    /**
     * 获取用户授权后，得到 code ，换取 access_token ，然后（自动注册帐号并）登录
     *
     * @param code 授权 code ，换取 Access_token 的凭据
     * @param response 响应，用于重定向
     * @throws IOException
     */
    @GetMapping(value = "/redirect")
    public void oauth(String code, HttpServletResponse response) throws IOException {
        // code 换取 Access_token
        TokenResponse tokenResponse = BaiduApiUtil.oauth(code, appKey, secretKey, redirectUri);
        String accessToken = tokenResponse.getAccessToken();
    
        // 获取百度用户信息
        UserInfoResponse userInfoResponse = BaiduApiUtil.userInfo(accessToken);
        
        // 登录本系统，（自动注册并）返回本系统用户信息
        signIn(userInfoResponse);
    
        // Access_token 保存到缓存，后续直接取用，不需要重新获取授权
        BaiduRedisUtil.cacheAccessToken(tokenResponse);
    
        response.sendRedirect(appHome);
    }
    
    /**
     * 使用百度授权信息登录本系统
     *
     * @param userInfoResponse  百度授权的用户信息
     */
    private void signIn(UserInfoResponse userInfoResponse) {
        // 以 uk 作为百度授权用户的 token ，通过 token 查找当前系统的用户，实现登录
        String token = String.valueOf(userInfoResponse.getUk());
        User user = userService.getByToken(token, TokenType.BAIDU);
        if (user == null) {
            // 首次授权登录，创建用户
            // 通过第三方登录创建的帐号，采用默认帐号（登录名）、密码（未经MD5加密），
            // 该账号、密码不可用于登录，必须通过第三方授权登录后修改帐号、密码（相当于注册帐号并绑定）
            user = new User().setToken(token)
                    .setTokenType(TokenType.BAIDU)
                    .setPasswd("default")
                    .setName("百度用户" + token);
            userService.add(user);
        }
        Subject subject = SecurityUtils.getSubject();
        BearerToken bearerToken = new BearerToken(token);
        ExpAuthorizingRealm.setTokenType(TokenType.BAIDU);
        subject.login(bearerToken);
    }
    
}
