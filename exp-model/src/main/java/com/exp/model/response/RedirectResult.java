package com.exp.model.response;

import com.exp.model.base.BaseResult;
import lombok.Getter;

/**
 * 表明结果需重定向到另一个请求获取
 */
@Getter
public class RedirectResult extends BaseResult {

    private final boolean redirect = true;
    
    /**
     * 重定向地址
     */
    private String url;
    
    public RedirectResult redirectTo(String url) {
        this.url = url;
        this.setMessage(url);
        return this;
    }
}
