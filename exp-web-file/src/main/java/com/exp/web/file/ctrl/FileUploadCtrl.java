package com.exp.web.file.ctrl;

import com.exp.toolkit.basic.StringUtils;
import com.exp.web.file.buffer.BufferPool;
import com.exp.web.file.buffer.BufferedMultipartFile;
import com.exp.web.file.buffer.BufferedMultipartResolver;
import com.exp.web.file.buffer.FileSegment;
import com.exp.web.file.pojo.FileUploadProgress;
import com.exp.model.response.ListResult;
import com.exp.model.response.MapResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class FileUploadCtrl {
    
    /**
     * 文件上传缓存；上传后的文件保存在缓存中，直到用户确认保存到指定位置，或超时清除；
     *
     * @param files
     * @param request
     * @return
     */
    @PostMapping("/buffer")
    public MapResult buffer(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        ListResult<BufferedMultipartFile> result = new ListResult<>();
        List<BufferedMultipartFile> fileList = new ArrayList<>();
        
        // 多文件上传
        for (MultipartFile file : files){
            BufferedMultipartFile bufferedFile = BufferPool.add((CommonsMultipartFile) file, request.getSession().getId());
            if (bufferedFile == null) {
                continue;
            }
            fileList.add(bufferedFile);
        }
        return new MapResult().put("fileList", fileList);
    }
    
    /**
     * 上传进度，指一次上传的整体进度，而不是单个文件的进度。
     * 多文件上传，每个文件都需要监控上传进度时，可以多次上传，每次上传一个文件。
     *
     * 20211213：此处只通过session中的progress属性记录，若是同时执行多个上传，岂不是混用了一个progress属性，造成混乱？
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/progress")
    public MapResult progress(HttpServletRequest request, String segmentId) {
        FileUploadProgress progress = (FileUploadProgress) request.getSession().getAttribute("segment-" + segmentId);
        if (progress == null) {
            progress = new FileUploadProgress().setRead(0L).setTotal(0L);
        }
        MapResult result = new MapResult();
        return result.put("progress", progress);
    }
    
    /**
     * 文件片段上传缓存；上传后的文件片段保存在缓存中，直到用户确认保存到指定位置，或超时清除；
     *
     * @param fileSegment
     * @param request
     * @return
     */
    @PostMapping(value = "/bufferSegment")
    public MapResult bufferSegment(FileSegment fileSegment, HttpServletRequest request) {
        CommonsMultipartFile segment = (CommonsMultipartFile) fileSegment.getSegment();
        String originalFilename = segment.getOriginalFilename();
        Integer round = fileSegment.getRound();
        Integer segNo = fileSegment.getSegNo();
        String uuid = fileSegment.getUuid();
        String id = fileSegment.getId();
        String fileId = uuid + id;
        BufferedMultipartFile bufferedFile = BufferPool.add(segment, request.getSession().getId());
        // TODO bufferedFile 关联 fileId 存到缓存（fileId 为键，值为有序集合，segment 按segNo为下标顺序存入，合并时按该顺序合并），业务上确定保存后，取出所有片段进行合并
        return new MapResult().put("file", bufferedFile);
    }
}
