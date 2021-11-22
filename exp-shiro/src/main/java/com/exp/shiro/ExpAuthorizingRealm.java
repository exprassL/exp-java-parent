package com.exp.shiro;

import com.exp.fluent.constant.TokenType;
import com.exp.fluent.entity.User;
import com.exp.service.impl.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * shiro登录与授权
 */
@Component
public class ExpAuthorizingRealm extends AuthorizingRealm {
    
    private static final ThreadLocal<TokenType> TOKEN_TYPE_THREAD_LOCAL = new ThreadLocal<>();
    
    @Resource
    private UserService userService;
    
    public static void setTokenType(TokenType tokenType) {
        TOKEN_TYPE_THREAD_LOCAL.set(tokenType);
    }
    
    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.fromRealm(getName()).iterator().next();
        ////        info.addStringPermissions(user....); FIXME 从user中获取权限列表进行授权
        return new SimpleAuthorizationInfo();
    }
    
    /**
     * 登录认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (authenticationToken instanceof BearerToken) {
            String token = ((BearerToken) authenticationToken).getToken();
            Object principal = userService.getByToken(token, TOKEN_TYPE_THREAD_LOCAL.get());
            return new SimpleAuthenticationInfo(principal, token, getName());

        } else if (authenticationToken instanceof UsernamePasswordToken) {
            String username = ((UsernamePasswordToken) authenticationToken).getUsername();
            if (username == null) {
                return null; // 这里返回后会报出对应异常
            }

//            获取用户信息
            User user = userService.getByUsername(username);
            if (user == null) {
                return null; // 这里返回后会报出对应异常
            } else {
//                这里验证authenticationToken和simpleAuthenticationInfo的信息
                return new SimpleAuthenticationInfo(user, user.getPasswd(), getName());
            }
        } else {
            return null;
        }
    }
}
