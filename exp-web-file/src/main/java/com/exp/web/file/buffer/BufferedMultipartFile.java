package com.exp.web.file.buffer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.File;
import java.io.IOException;

/**
 * 包装{@link CommonsMultipartResolver}，以支持文件预上传。
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(value = {"file"})
public class BufferedMultipartFile {

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 生命周期开始时间
     */
    private long lifeCycleStart;

    /**
     * 缓存文件
     */
    private CommonsMultipartFile file;
    
    /**
     * 文件落盘的路径
     */
    private String storage;

    public String getOriginalName() {
        return file.getOriginalFilename();
    }
    
    /**
     * 文件落盘
     * @throws IOException
     */
    public void transfer() throws IOException {
        file.transferTo(new File(storage));
    }
}
