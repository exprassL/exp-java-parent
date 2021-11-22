package com.exp.web.story.ctrl;

import com.exp.fluent.entity.File;
import com.exp.service.impl.AlbumService;
import com.exp.model.response.ListResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 相册操作
 */
@Slf4j
@Controller
@RequestMapping(value = "/album")
public class AlbumCtrl {
    
    @Resource
    private AlbumService albumService;
    
    /**
     * @param request
     * @param id      相册id
     * @param ids     缓存文件id
     * @return
     */
    @PostMapping(value = "/photo/add")
    @ResponseBody
    public ListResult<File> addPhotos(HttpServletRequest request, String id, String... ids) {
        return albumService.addPhotos(id, request.getSession().getId(), ids);
    }
}
