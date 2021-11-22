package com.exp.fluent.response;

import cn.org.atool.fluent.mybatis.base.BaseEntity;

import com.exp.model.base.BaseResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 数据分页
 *
 * @param <T>
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageResult<T extends BaseEntity> extends BaseResult {
    
    private int pageNo;
    
    private int pageSize;
    
    private int total;
    
    private List<T> dataList = new LinkedList<>();
    
    /**
     * 响应失败信息
     *
     * @param message
     * @return
     */
    public PageResult<T> fail(String message) {
        this.setSuccess(Boolean.FALSE);
        this.setMessage(message);
        return this;
    }
}