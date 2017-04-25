package com.hk.commons.web.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 辅助输出内容到前端
 * 处理一些@ResponseBody不方便设置参数情况
 * Created by linhy on 2017/4/24.
 */
public class ResponseUtils {
    // -- header 常量定义 --//
    /** The Constant HEADER_ENCODING. */
    private static final String HEADER_ENCODING = "encoding";

    /** The Constant HEADER_NOCACHE. */
    private static final String HEADER_NOCACHE = "no-cache";

    /** The Constant DEFAULT_ENCODING. */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /** The Constant DEFAULT_NOCACHE. */
    private static final boolean DEFAULT_NOCACHE = true;

    /** The mapper. */
    private static ObjectMapper mapper = new ObjectMapper();

    // -- 绕过jsp/freemaker直接输出文本的函数 --//
    /**
     * 直接输出内容的简便函数.
     *
     * eg. render("text/plain", "hello", "encoding:GBK"); render("text/plain",
     * "hello", "no-cache:false"); render("text/plain", "hello", "encoding:GBK",
     * "no-cache:false");
     *
     * @param contentType the content type
     * @param content the content
     * @param response the response
     * @param headers 可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
     */
    public static void render(final String contentType, final String content, HttpServletResponse response,
                              final String... headers) {
        response = initResponseHeader(contentType, response, headers);
        try {
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 直接输出文本.
     *
     * @param response the response
     * @param text the text
     * @param headers the headers
     * @see #render(String, String, String...)
     */
    public static void renderText(final HttpServletResponse response, final String text, final String... headers) {
        render(ServletUtils.TEXT_TYPE, text, response, headers);
    }

    /**
     * 直接输出HTML.
     *
     * @param response the response
     * @param html the html
     * @param headers the headers
     * @see #render(String, String, String...)
     */
    public static void renderHtml(final HttpServletResponse response, final String html, final String... headers) {
        render(ServletUtils.HTML_TYPE, html, response, headers);
    }

    /**
     * 直接输出XML.
     *
     * @param response the response
     * @param xml the xml
     * @param headers the headers
     * @see #render(String, String, String...)
     */
    public static void renderXml(final HttpServletResponse response, final String xml, final String... headers) {
        render(ServletUtils.XML_TYPE, xml, response, headers);
    }

    /**
     * 直接输出JSON.
     *
     * @param response the response
     * @param jsonString json字符串.
     * @param headers the headers
     * @see #render(String, String, String...)
     */
    public static void renderJson(final HttpServletResponse response, final String jsonString, final String... headers) {
        render(ServletUtils.JSON_TYPE, jsonString, response, headers);
    }

    /**
     * 直接输出JSON,使用Jackson转换Java对象.
     *
     * @param response the response
     * @param data 可以是List<POJO>, POJO[], POJO, 也可以Map名值对.
     * @param headers the headers
     * @see #render(String, String, String...)
     */
    public static void renderJson(HttpServletResponse response, final Object data, final String... headers) {
        response = initResponseHeader(ServletUtils.JSON_TYPE, response, headers);
        try {
            mapper.writeValue(response.getWriter(), data);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 直接输出支持跨域Mashup的JSONP.
     *
     * @param response the response
     * @param callbackName callback函数名.
     * @param object Java对象,可以是List<POJO>, POJO[], POJO ,也可以Map名值对, 将被转化为json字符串.
     * @param headers the headers
     */
    public static void renderJsonp(final HttpServletResponse response, final String callbackName, final Object object,
                                   final String... headers) {
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        String result = new StringBuilder().append(callbackName).append("(").append(jsonString).append(");").toString();

        // 渲染Content-Type为javascript的返回内容,输出结果为javascript语句,
        // 如callback197("{html:'Hello World!!!'}");
        render(ServletUtils.JS_TYPE, result, response, headers);
    }

    /**
     * 分析并设置contentType与headers.
     *
     * @param contentType the content type
     * @param response the response
     * @param headers the headers
     * @return the http servlet response
     */
    private static HttpServletResponse initResponseHeader(final String contentType, final HttpServletResponse response,
                                                          final String... headers) {
        // 分析headers参数
        String encoding = DEFAULT_ENCODING;
        boolean noCache = DEFAULT_NOCACHE;
        for (String header : headers) {
            String headerName = StringUtils.substringBefore(header, ":");
            String headerValue = StringUtils.substringAfter(header, ":");

            if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
                encoding = headerValue;
            } else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
                noCache = Boolean.parseBoolean(headerValue);
            } else {
                throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
            }
        }

        // 设置headers参数
        String fullContentType = contentType + ";charset=" + encoding;
        response.setContentType(fullContentType);
        if (noCache) {
            ServletUtils.setNoCacheHeader(response);
        }

        return response;
    }
}
