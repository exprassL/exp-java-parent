package com.exp.model.response;

import com.exp.model.base.BaseResult;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link Map}类型响应数据
 */
@Getter
public class MapResult extends BaseResult {
    
    private Map<String, Object> map = new HashMap<>();
    
    /**
     * 填充数据
     *
     * @param key
     * @param value
     * @return
     */
    public MapResult put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
    
    /**
     * 填充一组数据
     *
     * @param map
     * @return
     */
    public MapResult putAll(Map<String, Object> map) {
        this.map.putAll(map);
        return this;
    }
    
    /**
     * 响应失败信息
     *
     * @param message
     * @return
     */
    public MapResult fail(String message) {
        this.setSuccess(Boolean.FALSE);
        this.setMessage(message);
        return this;
    }
}