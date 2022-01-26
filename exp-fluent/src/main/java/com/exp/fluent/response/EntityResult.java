package com.exp.fluent.response;

import cn.org.atool.fluent.mybatis.base.BaseEntity;

import com.exp.model.base.BaseResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 实体类型响应数据
 *
 * @param <T> 实体类型
 */
@Getter
@Setter
public class EntityResult<T extends BaseEntity> extends BaseResult {

    private T data;
    
    /**
     * 填充实体数据
     *
     * @param entity
     * @return
     */
    public EntityResult<T> success(T entity) {
        this.data = entity;
        return this;
    }
    
    /**
     * 响应失败信息
     *
     * @param message
     * @return
     */
    public EntityResult<T> fail(String message) {
        this.setSuccess(Boolean.FALSE);
        this.setMessage(message);
        return this;
    }

}