package com.exp.shiro;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.exp.toolkit.basic.StringUtils;
import com.exp.web.exception.WebBizException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Shiro 登录及权限验证的相关链接转发。
 * 
 * @author Exprass
 */
@Controller
@RequestMapping(value = "/auth/*")
public class ShiroAuthController {

  /**
   * 未登录时去往此地址
   */
  @Value("${sso.auth.unauthenticated}")
  private String unauthenticatedUrl;

  /**
   * 未授权时去往此地址
   */
  @Value("${sso.auth.unauthorized}")
  private String unauthorizedUrl;

  /**
   * 未登录跳转
   * 
   * @param response
   * @throws IOException
   */
  @GetMapping(value = "/unauthenticated")
  public void unauthenticated(HttpServletResponse response) throws IOException {
    if (StringUtils.isBlank(unauthenticatedUrl)) {
      throw WebBizException.produce("未登录，请先登录");
    }

    response.sendRedirect(unauthenticatedUrl);
  }

  /**
   * 未授权跳转
   * 
   * @param response
   * @throws IOException
   */
  @GetMapping(value = "/unauthorized")
  public void unauthorized(HttpServletResponse response) throws IOException {
    if (StringUtils.isBlank(unauthorizedUrl)) {
      throw WebBizException.produce("没有访问权限");
    }

    response.sendRedirect(unauthorizedUrl);
  }
}