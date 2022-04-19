package com.study.bootaop.interceptor;

import com.study.bootaop.config.RequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * ClassName: ReplaceStreamFilter
 * Description: 替换HttpServletRequest
 *
 * @Author: luohx
 * Date: 2022/4/19 下午5:41
 * History:
 * <author>          <time>          <version>          <desc>
 * luohx            修改时间           1.0           替换HttpServletRequest
 */
public class ReplaceStreamFilter implements Filter {

    /**
     * 包装Request
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
    }
}
