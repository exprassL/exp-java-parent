package com.exp.service;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * Service 层基类
 *
 * @param <E>  实体类型
 * @param <PK> 主键类型
 * @noinspection unused
 */
public abstract class AbstractBaseService<E extends IEntity, PK extends Serializable> {
    
    protected IBaseDao<E> baseDao;
    
    /**
     * 新增实体
     *
     * @param e 实体
     * @return 主键
     */
    public PK add(E e) {
        //noinspection unchecked
        return (PK) baseDao.save(e);
    }
    
    /**
     * 批量新增实体
     *
     * @param es 实体集合
     * @return
     */
    public int addAll(Collection<E> es) {
        Collection<E> esWithPk = new LinkedList<>();
        Iterator<E> iterator = es.iterator();
        while (iterator.hasNext()) {
            E next = iterator.next();
            if (next.findPk() != null) {
                esWithPk.add(next);
                iterator.remove();
            }
        }
        int count = 0, countWithPk = 0;
        //noinspection unchecked
        IRichMapper<E> mapper = baseDao.mapper();
        if (es.size() > 0) {
            count = mapper.insertBatch(es);
        }
        if (esWithPk.size() > 0) {
            countWithPk = mapper.insertBatchWithPk(esWithPk);
        }
        return count + countWithPk;
    }
    
    /**
     * 根据主键决定插入或更新实体记录：
     * <li>主键存在：更新记录</li>
     * <li>主键不存在：插入记录</li>
     *
     * @param e
     * @return
     */
    public boolean saveOrUp(E e) {
        return baseDao.saveOrUpdate(e);
    }
    
    /**
     * 根据主键查询
     *
     * @param id 主键值
     * @return 找到的实体
     */
    public E get(PK id) {
        return baseDao.selectById(id);
    }
    
    /**
     * 根据主键列表查询
     *
     * @param ids 主键列表
     * @return 实体列表
     */
    @SafeVarargs
    public final List<E> selectByIds(PK... ids) {
        return baseDao.selectByIds(ids);
    }
    
    /**
     * 判断主键是否已存在实体记录
     *
     * @param id 主键值
     * @return 是：存在， 否：不存在
     */
    public boolean existPk(PK id) {
        return baseDao.existPk(id);
    }
    
    /**
     * 根据实体主键修改实体非 null 属性
     *
     * @param e 实体
     * @return 更新是否成功
     */
    public boolean updateNonPropsById(E e) {
        return baseDao.updateById(e);
    }
    
    /**
     * 根据实体主键修改实体非 null 属性，批量操作
     *
     * @param es 实体集合
     * @return 更新是否成功
     */
    @SafeVarargs
    public final boolean updateNonPropsByIds(E... es) {
        return baseDao.updateEntityByIds(es);
    }
    
    /**
     * 根据实体 id 删除，批量操作
     *
     * @param es 实体集合
     * @return 删除数量
     */
    public int deleteByEntityIds(Collection<E> es) {
        return baseDao.deleteByEntityIds(es);
    }
    
    /**
     * 根据实体 id 删除，批量操作
     *
     * @param es 实体列表
     * @return 删除数量
     */
    @SafeVarargs
    public final int deleteByEntityIds(E... es) {
        return baseDao.deleteByEntityIds(es);
    }
    
    /**
     * 根据 id 删除，批量操作
     *
     * @param ids 主键集合
     * @return 删除数量
     */
    public int deleteByIds(Collection<PK> ids) {
        return baseDao.deleteByIds(ids);
    }
    
    /**
     * 根据 id 删除，批量操作
     *
     * @param ids 主键列表
     * @return 删除数量
     */
    @SafeVarargs
    public final int deleteByIds(PK... ids) {
        return baseDao.deleteByIds(Collections.singleton(ids));
    }
    
    /**
     * 根据实体 id 逻辑删除，批量操作
     *
     * @param es 实体集合
     * @return 删除数量
     */
    public int logicalDeleteEntityByIds(Collection<E> es) {
        return baseDao.logicDeleteByEntityIds(es);
    }
    
    /**
     * 根据实体 id 逻辑删除，批量操作
     *
     * @param es 实体列表
     * @return 删除数量
     */
    @SafeVarargs
    public final int logicalDeleteEntityByIds(E... es) {
        return baseDao.logicDeleteByEntityIds(es);
    }
    
    /**
     * 根据 id 逻辑删除，批量操作
     *
     * @param ids 主键集合
     * @return 删除数量
     */
    public int logicalDeleteByIds(Collection<PK> ids) {
        return baseDao.logicDeleteByIds(ids);
    }
    
    /**
     * 根据 id 逻辑删除，批量操作
     *
     * @param ids 主键列表
     * @return 删除数量
     */
    @SafeVarargs
    public final int logicalDeleteByIds(PK... ids) {
        //noinspection ConfusingArgumentToVarargsMethod
        return baseDao.mapper().logicDeleteById(ids);
    }
}
