package com.exp.web.sso.ctrl;

import com.exp.service.impl.UserService;
import com.exp.toolkit.security.MD5Util;
import com.exp.toolkit.security.RSAUtil;
import com.exp.model.response.MapResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录控制器
 */
@RestController
@RequestMapping("/sign")
public class SignCtrl {

    @Resource
    private UserService userService;
    
    /**
     * 返回默认公钥串，用于登陆时加密密码
     *
     * @return 默认公钥串
     */
    @GetMapping("/key")
    public MapResult publicKey() {
        return new MapResult().put("key", RSAUtil.getDefaultPublicKey());
    }
    
    /**
     * 用户名密码登录
     *
     * @param username  用户名
     * @param passwd    RSA加密的密码密码
     * @return
     */
    @GetMapping("/in/{username}/{passwd}")
    public MapResult signIn(@PathVariable String username, @PathVariable String passwd) {
        String md5 = MD5Util.md5(RSAUtil.decrypt(passwd));
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, md5);
        subject.login(token);
        return new MapResult();
    }
    
    /**
     * 通过token登录
     * @return
     */
    @GetMapping("/in")
    public MapResult signInFromToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        return new MapResult();
    }
}