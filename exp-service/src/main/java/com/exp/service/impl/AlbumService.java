package com.exp.service.impl;

import com.exp.fluent.dao.intf.AlbumDao;
import com.exp.fluent.dao.intf.FileDao;
import com.exp.fluent.entity.Album;
import com.exp.fluent.entity.AlbumFile;
import com.exp.fluent.entity.File;
import com.exp.model.response.ListResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 相册业务
 */
@Slf4j
@Service
@Transactional
public class AlbumService {
    
    @Resource
    private AlbumDao albumDao;
    
    @Resource
    private FileDao fileDao;
    
    /**
     * 新增一个相册
     *
     * @param album 待新增的相册
     * @return  新增的相册
     */
    public Album add(Album album) {
        albumDao.save(album);
        return album;
    }
    
    /**
     * 获取相册信息，根据id
     *
     * @param id    相册id
     * @return      相册
     */
    public Album get(Integer id) {
        return albumDao.selectById(id);
    }
    
    /**
     * 图片文件关联到相册
     *
     * @param id        album id
     * @param sessionId session id
     * @param ids       缓存文件id
     * @return  包含图片信息的File实体
     */
    public ListResult<File> addPhotos(String id, String sessionId, String... ids) {
        List<File> fileList = new ArrayList<>();
//        IFileUploadService.super.store(UploadType.ALBUM, BufferPool.rootDirectory, sessionId, ids)
//                .getDataList().stream().forEach(f -> {
//
//            File file = new File();
//            String fileName = f.getOriginalName();
//            String storage = f.getStorage();
//            file.setOriginalName(fileName)
//                    .setFileExt(fileName.substring(fileName.lastIndexOf(".")))
//                    .setFileSize(f.getFile().getSize())
//                    .setStorageName(storage.substring(storage.lastIndexOf(java.io.File.separator) + 1))
//                    .setStoragePath(storage).setMd5("");
//        });
        int saved = fileDao.save(fileList);
        List<AlbumFile> albumFileList = new ArrayList<>();
        return null;
    }
    
}
