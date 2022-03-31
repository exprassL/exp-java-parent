package com.exp.web.story.service;

import com.exp.fluent.entity.Album;
import com.exp.fluent.entity.File;
import com.exp.fluent.mapper.FileMapper;
import com.exp.model.response.ListResult;
import com.exp.service.impl.AlbumService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.exp"})
@MapperScan(basePackageClasses = FileMapper.class)
public class AlbumServiceTest {
    
    private static final Logger logger = LoggerFactory.getLogger("test");
    
    @Resource
    private AlbumService albumService;
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testAdd() {
        Album album = new Album().setName("My First Album");
        Integer id = albumService.add(album);
        Assert.assertNotNull(album);
        Assert.assertNotNull(album.getId());
    }
    
    @Test
    public void testGet() {
        Album album = albumService.get(2);
        Assert.assertNotNull(album);
        logger.info("OK: {}", album);
        List<File> fileList = album.findFileList();
        logger.info("OK: {}", fileList);
    }
    
    @Test
    public void testAddPhotos() {
        ListResult<File> photos = albumService.addPhotos("1", "afjas", "0980r98");
        assertNotNull(photos.getData());
        logger.info("保存File实体数量：{}", photos.getData().size());
        assertTrue(photos.getSuccess());
    }
}