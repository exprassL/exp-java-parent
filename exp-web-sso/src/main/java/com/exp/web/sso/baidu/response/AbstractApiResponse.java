package com.exp.web.sso.baidu.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public abstract class AbstractApiResponse {
    
    /**
     * 错误码， 0 表示成功
     */
    private String errno;
    
    /**
     * 错误信息
     */
    private String errmsg;
    
    private long requestId;
}
