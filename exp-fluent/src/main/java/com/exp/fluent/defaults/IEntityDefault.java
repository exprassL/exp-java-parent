package com.exp.fluent.defaults;

/**
 * 通用接口，定义一些getter和setter。
 */
public interface IEntityDefault {
    
    /**
     * 返回创建人
     *
     * @return
     */
    Integer getCreatedBy();
    
    /**
     * 设置创建人
     *
     * @param createdBy
     * @return
     */
    IEntityDefault setCreatedBy(Integer createdBy);
    
    /**
     * 设置最后更新人（getter不必要，因为setter前不必判断是否已设置，直接覆盖更新）
     *
     * @param lastUpdatedBy
     * @return
     */
    IEntityDefault setLastUpdatedBy(Integer lastUpdatedBy);
    
    /**
     * 返回entity env属性值
     *
     * @return
     */
    String getEnv();
    
    /**
     * 设置entity env属性值
     *
     * @param env
     * @return
     */
    IEntityDefault setEnv(String env);
    
    /**
     * 返回entity 租户信息
     *
     * @return
     */
    String getTenant();
    
    /**
     * 设置entity 租户信息
     *
     * @param tenant
     * @return
     */
    IEntityDefault setTenant(String tenant);
    
}
