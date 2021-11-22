package com.exp.model.response;


import com.exp.model.base.BaseResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 异常时的响应
 */
@Getter
@Setter
public class ErrorResult extends BaseResult {
    
    /**
     * @param message 错误信息
     * @return
     */
    public ErrorResult fail(String message) {
        super.setSuccess(Boolean.FALSE);
        super.setMessage(message);
        return this;
    }
}