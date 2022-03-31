package com.exp.web.sso.service;

import com.exp.fluent.constant.TokenType;
import com.exp.fluent.entity.User;
import com.exp.fluent.mapper.FileMapper;
import com.exp.service.impl.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.exp"})
@MapperScan(basePackageClasses = FileMapper.class)
public class UserServiceTest {
    
    @Resource
    private UserService userService;
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void add() {
    }
    
    @Test
    public void addAll() {
    }
    
    @Test
    public void saveOrUp() {
    }
    
    @Test
    public void get() {
        User user = userService.get(1);
        Assert.assertNotNull(user);
    }
    
    @Test
    public void selectByIds() {
    }
    
    @Test
    public void existPk() {
    }
    
    @Test
    public void updateNonPropsById() {
    }
    
    @Test
    public void updateNonPropsByIds() {
    }
    
    @Test
    public void deleteByEntityIds() {
    }
    
    @Test
    public void testDeleteByEntityIds() {
    }
    
    @Test
    public void deleteByIds() {
    }
    
    @Test
    public void testDeleteByIds() {
    }
    
    @Test
    public void logicalDeleteEntityByIds() {
    }
    
    @Test
    public void testLogicalDeleteEntityByIds() {
    }
    
    @Test
    public void logicalDeleteByIds() {
    }
    
    @Test
    public void testLogicalDeleteByIds() {
    }
    
    @Test
    public void getByToken() {
        String token = "3540951611";
        User user = userService.getByToken(token, TokenType.BAIDU);
        Assert.assertNotNull(user);
    }
    
    @Test
    public void getByUsername() {
    }
}