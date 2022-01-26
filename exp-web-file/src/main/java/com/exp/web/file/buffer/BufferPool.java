package com.exp.web.file.buffer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.Assert;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 临时文件关联缓存到对应的session下，以支持文件预上传。即：
 * <ol>
 * <li>上传文件，缓存到此，Servlet结束时不销毁文件
 * <li>根据选择的缓存文件id，找到对应文件，存储到服务器，并执行其他业务</li>
 * <li>未选择的文件，在Session结束、超时或缓存超时（时间可配置）时清除</li>
 * </ol>
 * <br>
 * 每个用户线程操作{@link #pool}中的不同的数据{@link Map}，因此相互之间无论缓存{@link #add(CommonsMultipartFile, String)}、
 * 获取{@link #getSessionFiles(String, String...)}或者session清理{@link #clean(String)}，都没有并发问题，但守护线程超时清理
 * {@link #cleanTimeOut()}（定时器），与上述前2中操作存在并发问题（），因此分别使用{@link #readLock}和{@link #writeLock}管理并发。
 * <br>
 * FIXME 以下：
 * 由于没有使用缓存中间件，数据直接缓存在内存中，随着上传文件用户session数量的增加，内存消耗增加；
 * 若使用缓存中间件，文件保存到本地（内存？或磁盘？），文件信息（含句柄，指向内存地址）转为{@link Map}保存到缓存，
 * 使用时，从缓存取出的文件信息句柄，其指向的内存实体是否已被GC回收，能否从其中恢复文件？
 */
@Slf4j
@Configuration
public class BufferPool implements InitializingBean, DisposableBean {
    
    /**
     * 文件缓存持
     */
    private static final Map<String, Map<String, BufferedMultipartFile>> pool = new HashMap<>();
    
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();
    
    public static String rootDirectory;
    private static long bufferThreshold;
    private static boolean clean;
    
    @Resource(type = BufferedMultipartResolver.class)
    private CommonsMultipartResolver multipartResolver;
    
    /**
     * 从缓存种获取文件
     *
     * @param sessionId
     * @param ids
     * @return
     */
    public static List<BufferedMultipartFile> getSessionFiles(String sessionId, String... ids) {
        log.info("获取缓存的文件：sessionId={}，ids.length={}", sessionId, ids.length);
        readLock.lock();
        try {
            List<BufferedMultipartFile> result = new ArrayList<>();
            Map<String, BufferedMultipartFile> map = pool.get(sessionId);
            for (String id : ids) {
                log.info("获取缓存的文件：fileId={}", id);
                BufferedMultipartFile file = map.get(id);
                result.add(file);
            }
            log.info("获取缓存的文件：完成");
            return result;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 将MultipartFile放入缓存。
     *
     * @param file
     * @return 文件唯一标识
     * @throws Exception
     */
    public static BufferedMultipartFile add(CommonsMultipartFile file, String sessionId) {
        log.info("缓存文件：sessionId={}", sessionId);
        
        if (file == null) {
            log.info("缓存文件：文件不存在");
            return null;
        }
        
        readLock.lock();
        try {
            Map<String, BufferedMultipartFile> map = pool.computeIfAbsent(sessionId, k -> new HashMap<>());
            
            String id = BufferedMultipartResolver.getSegmentId();
            BufferedMultipartFile bufferedFile = new BufferedMultipartFile();
            bufferedFile.setId(id);
            bufferedFile.setFile(file);
            bufferedFile.setLifeCycleStart(System.currentTimeMillis());
            map.put(id, bufferedFile);
            log.info("缓存文件：fileId={}", id);
            return bufferedFile;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 用户注销、Session超时时清理缓存的文件
     *
     * @param sessionId
     */
    public static void clean(String sessionId) {
        if (!BufferPool.clean) {
            log.info("session[{}]结束，根据配置，缓存文件不做清理", sessionId);
            return;
        }
        Assert.hasText(sessionId, "SessionID为空");
        
        log.info("session[{}]结束，清理缓存文件", sessionId);
        pool.remove(sessionId);
        // TODO 需手动删除临时文件，否则程序退出导致session结束时，程序可能来不及删除文件
        log.info("session[{}]结束，缓存文件已清理", sessionId);
    }
    
    /**
     * 根据超时阈值清理缓存文件
     */
    public static void cleanTimeOut() {
        long now = System.currentTimeMillis();
        log.info("缓存文件超时清理：当前时间={}", now);
        
        writeLock.lock();
        
        try {
            Iterator<Map.Entry<String, Map<String, BufferedMultipartFile>>> iterator = pool.entrySet().iterator();
            
            while (iterator.hasNext()) {
                Map.Entry<String, Map<String, BufferedMultipartFile>> next = iterator.next();
                String sessionId = next.getKey();
                log.info("缓存文件超时清理：当前时间={}，sessionId={}", now, sessionId);
                
                Map<String, BufferedMultipartFile> fileMap = next.getValue();
                if (fileMap == null) {
                    log.info("缓存文件超时清理：当前时间={}，sessionId={}，session没有缓存文件", now, sessionId);
                    iterator.remove();
                    continue;
                }
    
                Iterator<Map.Entry<String, BufferedMultipartFile>> it = fileMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, BufferedMultipartFile> nx = it.next();
                    String fileId = nx.getKey();
                    BufferedMultipartFile file = nx.getValue();
                    if (file == null) {
                        log.info("缓存文件超时清理：当前时间={}，sessionId={}，fileId={}，缓存文件为空", now, sessionId, fileId);
                        it.remove();
                        continue;
                    }
                    
                    long lifeCycleStart = file.getLifeCycleStart();
                    log.info("缓存文件超时清理：当前时间={}，sessionId={}，fileId={}，生命周期开始={}", now, sessionId, fileId, lifeCycleStart);
                    if (lifeCycleStart + BufferPool.bufferThreshold <= now) {
                        file.getFile().getFileItem().delete();
                        it.remove();
                        log.info("缓存文件超时清理：当前时间={}，sessionId={}，fileId={}，生命周期开始={}，超时删除", now, sessionId, fileId, lifeCycleStart);
                    }
                }
                
                if (fileMap.size() == 0) {
                    log.info("缓存文件超时清理：当前时间={}，sessionId={}，session缓存文件已清空，删除session缓存", now, sessionId);
                    iterator.remove();
                }
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 清空缓存并删除缓存文件
     */
    private static void cleanAll() {
        Iterator<Map.Entry<String, Map<String, BufferedMultipartFile>>> iterator = pool.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Map<String, BufferedMultipartFile>> next = iterator.next();
            String sessionId = next.getKey();
            log.info("缓存文件销毁：sessionId={}", sessionId);
            
            Map<String, BufferedMultipartFile> fileMap = next.getValue();
            if (fileMap == null) {
                log.info("缓存文件销毁：sessionId={}，session没有缓存文件", sessionId);
                iterator.remove();
                continue;
            }
    
            Iterator<Map.Entry<String, BufferedMultipartFile>> it = fileMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, BufferedMultipartFile> nx = it.next();
                String fileId = nx.getKey();
                BufferedMultipartFile file = nx.getValue();
                if (file == null) {
                    log.info("缓存文件销毁：sessionId={}，fileId={}，缓存文件为空", sessionId, fileId);
                    it.remove();
                    return;
                }
                
                file.getFile().getFileItem().delete();
                it.remove();
                log.info("缓存文件销毁：sessionId={}，fileId={}，文件已销毁", sessionId, fileId);
            }
            
            if (fileMap.size() == 0) {
                log.info("缓存文件销毁：sessionId={}，session缓存文件已清空，删除session缓存", sessionId);
                iterator.remove();
            }
        }
    }
    
    /**
     * 启动定时清理
     */
    private static void init() {
        if (!BufferPool.clean) {
            return;
        }
        
        TimerTask task = new TimerTask() {
            
            @Override
            public void run() {
                cleanTimeOut();
            }
        };
        
        Timer timer = new Timer("CleanBufferedMultiPartFile", true);
        timer.schedule(task, bufferThreshold, bufferThreshold);
    }
    
    /**
     * 文件缓存保持时长，默认60分钟
     */
    @Value("${file.upload.resolver.buffer-threshold:60}")
    private void setBufferThreshold(long bufferThreshold) {
        BufferPool.bufferThreshold = bufferThreshold * 60L * 1000L;
    }
    
    /**
     * 配置文件存放目录，包括临时文件缓存目录
     *
     * @param path
     * @throws IOException
     */
    @Value("${file.storage.root-directory:/data/FileUpload/}")
    private void setPath(String path) throws IOException {
        BufferPool.rootDirectory = path;
        String tempPath = path + "TEMP";
        File file = new java.io.File(tempPath);
        if (!file.exists() && !file.mkdirs()) {
            throw new IllegalArgumentException("Given uploadTempDir [" + file + "] could not be created");
        }
        FileSystemResource resource = new FileSystemResource(file);
        multipartResolver.setUploadTempDir(resource);
        log.info("临时文件存放目录：{}", tempPath);
        //设置文件删除追踪器为null
        multipartResolver.getFileItemFactory().setFileCleaningTracker(null); // TODO 有何用处？可否删除
    }
    
    /**
     * 初始化清理缓存的开关，未配置或配置为<code>true</code>表示开启开关。
     */
    @Value("${file.upload.resolver.clean}")
    private void setClean(Boolean clean) {
        // 初始化属性
        BufferPool.clean = clean == null || clean;
        log.info("缓存清理开关：{}", BufferPool.clean);
    }
    
    /**
     * bean销毁时
     *
     * @throws Exception
     */
    @Override
    public void destroy() {
        BufferPool.cleanAll();
    }
    
    /**
     * bean属性完成配置后
     */
    @Override
    public void afterPropertiesSet() {
        BufferPool.init();
    }
}
