package com.exp.model.response;

import com.exp.model.base.BaseResult;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * 集合类型响应数据
 *
 * @param <T> 集合中的数据类型
 */
@Getter
@Setter
public class ListResult<T> extends BaseResult {
    
    private List<T> dataList = new LinkedList<>();
    
    /**
     * 填充一个数据
     *
     * @param element 待填充元素
     * @return
     */
    public ListResult<T> add(T element) {
        this.dataList.add(element);
        return this;
    }
    
    /**
     * 填充一组数据
     *
     * @param elements 待填充元素
     * @return
     */
    public ListResult<T> addAll(List<T> elements) {
        this.dataList.addAll(elements);
        return this;
    }
    
    /**
     * 响应失败信息
     *
     * @param message
     * @return
     */
    public ListResult<T> fail(String message) {
        this.setSuccess(Boolean.FALSE);
        this.setMessage(message);
        return this;
    }
}