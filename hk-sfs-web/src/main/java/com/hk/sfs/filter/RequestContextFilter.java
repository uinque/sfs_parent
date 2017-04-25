package com.hk.sfs.filter;


import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * @date 2017/4/22
 */
@WebFilter(filterName="requestContextFilter",urlPatterns="/*")
public class RequestContextFilter extends OncePerRequestFilter implements Filter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RequestContext rc = RequestContext.begin(getServletContext(), request, response);
        try{
            before(request, response, filterChain, rc);
            filterChain.doFilter(request, response);
            after(request, response, filterChain, rc);
        }finally {
            if (rc != null){
                rc.end();
            }
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * Before.
     *
     * @param request the request
     * @param response the response
     * @param filterChain the filter chain
     * @param rc the rc
     */
    protected void before(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                          RequestContext rc) {

    }

    /**
     * After.
     *
     * @param request the request
     * @param response the response
     * @param filterChain the filter chain
     * @param rc the rc
     */
    protected void after(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                         RequestContext rc) {

    }
}
