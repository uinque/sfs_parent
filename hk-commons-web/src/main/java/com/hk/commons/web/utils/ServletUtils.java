package com.hk.commons.web.utils;

import org.apache.commons.lang3.Validate;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 	servlet的一些工具类
 * @author 	linhy
 */
public abstract class ServletUtils {

    // -- Content Type 定义 --//
    /** The Constant TEXT_TYPE. */
    public static final String TEXT_TYPE = "text/plain";

    /** The Constant JSON_TYPE. */
    public static final String JSON_TYPE = "application/json";

    /** The Constant XML_TYPE. */
    public static final String XML_TYPE = "text/xml";

    /** The Constant HTML_TYPE. */
    public static final String HTML_TYPE = "text/html";

    /** The Constant JS_TYPE. */
    public static final String JS_TYPE = "text/javascript";

    /** The Constant EXCEL_TYPE. */
    public static final String EXCEL_TYPE = "application/vnd.ms-excel";

    // -- Header 定义 --//
    /** The Constant AUTHENTICATION_HEADER. */
    public static final String AUTHENTICATION_HEADER = "Authorization";

    // -- 常用数值定义 --//
    /** The Constant ONE_YEAR_SECONDS. */
    public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

    public static void setNoCacheHeader(HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader("Expires", 0);
        response.addHeader("Pragma", "no-cache");
        // Http 1.1 header
        response.setHeader("Cache-Control", "no-cache");
    }

    /**
     * 设置让浏览器弹出下载对话框的Header,不同浏览器使用不同的编码方式.
     *
     * @param fileName 下载后的文件名.
     */
    public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        final String CONTENT_DISPOSITION = "Content-Disposition";

        try {
            String agent = request.getHeader("User-Agent");
            String encodedfileName = null;
            if (null != agent) {
                agent = agent.toLowerCase();
                if (agent.contains("firefox") || agent.contains("chrome") || agent.contains("safari")) {
                    encodedfileName = "filename=\"" + new String(fileName.getBytes(), "ISO8859-1") + "\"";
                } else if (agent.contains("msie")) {
                    encodedfileName = "filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"";
                } else if (agent.contains("opera")) {
                    encodedfileName = "filename*=UTF-8\"" + fileName + "\"";
                } else {
                    encodedfileName = "filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"";
                }
            }

            response.setHeader(CONTENT_DISPOSITION, "attachment; " + encodedfileName);
        } catch (UnsupportedEncodingException e) {
        }
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     *
     * 返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Validate.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        if (prefix == null) {
            prefix = "";
        }
        while ((paramNames != null) && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if ((values == null) || (values.length == 0)) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /**
     * 组合Parameters生成Query String的Parameter部分, 并在paramter name上加上prefix.
     *
     * @see #getParametersStartingWith
     */
    public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
        if ((params == null) || (params.size() == 0)) {
            return "";
        }

        if (prefix == null) {
            prefix = "";
        }

        StringBuilder queryStringBuilder = new StringBuilder();
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            queryStringBuilder.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
            if (it.hasNext()) {
                queryStringBuilder.append('&');
            }
        }
        return queryStringBuilder.toString();
    }

    /**
     * 返回客户端IP
     * @param request
     * @return
     */
    public static String getRemortIP(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forward-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
