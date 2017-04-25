package com.hk.sfs.conf;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 自定义异常页面配置类
 * @author Administrator
 * @date 2017/4/23
 */
@Component
public class ErrorConfiguration {

    /**
     * 自定义异常处理路径
     * @return
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return (container -> {
            ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400");
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401");
            ErrorPage error403Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/403");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");

            container.addErrorPages(error400Page, error401Page, error403Page, error404Page, error500Page);
        });
    }
}
