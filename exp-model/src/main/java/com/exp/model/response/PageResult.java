package com.exp.model.response;


import com.exp.model.base.BaseEntity;
import com.exp.model.base.BaseResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
    
    private Page<T> data = new Page<>();
    
    public PageResult<T> buildPage(int pageNo, int pageSize, int total, List<T> rows) {
        this.data.setPageNo(pageNo).setPageSize(pageSize).setTotal(total).setRows(rows);
        return this;
    }
    
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
    
    /**
     * 分页对象
     *
     * @param <T>
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    private static class Page<T> implements Serializable {
        private static final long serialVersionUID = 5574986699035619554L;
        
        private int pageNo;
        
        private int pageSize;
        
        private int total;
        
        private List<T> rows = new LinkedList<>();
    }
}