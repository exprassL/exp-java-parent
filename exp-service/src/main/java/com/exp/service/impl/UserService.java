package com.exp.service.impl;

import com.exp.fluent.constant.TokenType;
import com.exp.fluent.dao.intf.UserDao;
import com.exp.fluent.entity.User;
import com.exp.fluent.wrapper.UserQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户信息Service
 */
@Slf4j
@Service
@Transactional
public class UserService {
    
    @Resource
    private UserDao userDao;
    
    /**
     * 根据token查找唯一用户。
     *
     * @param token
     * @param tokenType
     * @return
     */
    public User getByToken(String token, TokenType tokenType) {
        return (User) userDao.mapper().findOne(
                new UserQuery().where.token().eq(token).and.tokenType().eq(tokenType).end());
    }
    
    /**
     * 根据用户名username查找唯一用户
     *
     * @param username
     * @return
     */
    public User getByUsername(String username) {
        return (User) userDao.mapper().findOne(new UserQuery().where.username().eq(username).end());
    }
}