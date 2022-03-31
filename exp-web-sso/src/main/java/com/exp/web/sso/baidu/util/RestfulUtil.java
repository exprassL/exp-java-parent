package com.exp.web.sso.baidu.util;

import com.exp.web.exception.WebBizException;
import com.exp.web.sso.baidu.response.AbstractApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

/**
 * 封装 RestTemplate 对百度 api 的调用
 */
@Slf4j
public final class RestfulUtil {
    
    private static final RestTemplate template = new RestTemplate();
    
    /**
     * 调用 restful get 接口
     *
     * @param url       接口地址
     * @param uriParams uri 参数
     * @param type      响应数据类型对应的 Class 类对象
     * @param <T>       响应数据类型
     * @return
     */
    public static <T extends AbstractApiResponse> T doGet(String url, Class<T> type, Object... uriParams) {
        
        T body = template.getForObject(url, type, uriParams);
        if (body == null) {
            log.error("接口[{}]响应为空，参数：{}", url, uriParams);
            throw WebBizException.produce("接口[%s]响应为空", url);
        }
        
        String errno = body.getErrno();
        if ("0".equals(errno)) {
            return body;
        } else {
            log.error("接口[{}]响应错误码：{}，参数：{}", url, errno, uriParams);
            throw WebBizException.produce("接口[%s]响应错误码：{%s}", url, uriParams);
        }
    }
}
