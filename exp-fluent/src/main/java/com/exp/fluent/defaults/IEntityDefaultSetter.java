package com.exp.fluent.defaults;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import com.exp.web.configer.WebAppEnvironment;

/**
 *
 */
public interface IEntityDefaultSetter extends IDefaultSetter {
    
    /**
     * 插入的entity，设置创建人、最后更新人、环境和租户。
     *
     * @param entity
     */
    @Override
    default void setInsertDefault(IEntity entity) {
        IEntityDefault iDefault = (IEntityDefault) entity;
        // 创建人、最后更新人设为当前登录人
        iDefault.setCreatedBy(1); // TODO 获取当前登录人，shiro?
        iDefault.setLastUpdatedBy(1); // TODO 同上
        
        // 环境未设置，则设为当前运行环境
        iDefault.setEnv(WebAppEnvironment.currentEnv());
        
        // 租户未设置，则设为当前用户所属租户
//        iDefault.setTenant(TenantUtils.findUserTenant());
        iDefault.setTenant("1"); // TODO 根据用户获取租户
    }
    
    /**
     * 查询条件排除逻辑删除记录，并追加环境隔离和租户隔离
     *
     * @param query
     */
    @Override
    default void setQueryDefault(IQuery query) {
        query.where()
                .apply("logical_deleted", SqlOp.EQ, Boolean.FALSE)
                .apply("env", SqlOp.EQ, WebAppEnvironment.currentEnv())
//                .apply("tenant", SqlOp.EQ, TenantUtils.findUserTenant());
                .apply("tenant", SqlOp.EQ, 1L); // TODO 根据用户获取租户
    }
    
    /**
     * 更新条件追加环境隔离和租户隔离
     *
     * @param updater
     */
    @Override
    default void setUpdateDefault(IUpdate updater) {
        updater.where()
                .apply("env", SqlOp.EQ, WebAppEnvironment.currentEnv())
//                .apply("tenant", SqlOp.EQ, TenantUtils.findUserTenant());
                .apply("tenant", SqlOp.EQ, 1L); // TODO 根据用户获取租户
    }
}
