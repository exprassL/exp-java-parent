package com.exp.web.ctrl;

import com.exp.model.response.MapResult;
import com.exp.toolkit.basic.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用ctrl
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonCtrl {
    
    /**
     * 返回uuid
     *
     * @return
     */
    @GetMapping(value = "/uuid")
    public MapResult uuid() {
        return new MapResult().put("uuid", StringUtils.uuid());
    }
}
