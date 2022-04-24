package com.exp.web.story.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.exp.fluent.entity.File;
import com.exp.model.response.ListResult;
import com.exp.service.impl.AlbumService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 相册操作
 */
@RestController
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
    public ListResult<File> addPhotos(HttpServletRequest request, String id, String... ids) {
        return albumService.addPhotos(id, request.getSession().getId(), ids);
    }
}
