package com.exp.web.sso.baidu.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息接口回参
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoResponse extends AbstractApiResponse {
    
    /**
     * 用户id
     */
    private long uk;
    
    /**
     * 百度帐号
     */
    private String baiduName;
    
    /**
     * 网盘帐号
     */
    private String netdiskName;
    
    /**
     * 头像地址
     */
    private String avatarUrl;
    
    /**
     * 会员类型： 0-普通用户，1-普通会员，2-超级会员
     */
    private int vipType;
}
