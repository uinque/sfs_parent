package com.hk.commons.utils.http;


import org.apache.commons.lang3.RandomUtils;
import org.apache.http.Consts;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;

/**
 * httpclient简单封装
 * Created by linhy on 2017/4/20.
 */
public class HttpClientUtils {

    public final static String DEFAULT_ENCODE = "UTF-8";
    private static final String[] USER_AGENTS = new String[]{
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.102 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1309.0 Safari/537.17",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.94 Safari/537.36 OPR/24.0.1558.53"
    };

    private static final HttpClient HTTP_CLIENT;
    private static final RequestConfig DEFAULT_REQUEST_CONFIG;

    static {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        DEFAULT_REQUEST_CONFIG =
                RequestConfig.custom()
                        .setExpectContinueEnabled(false) // 设置不使用Expect:100-Continue握手
                        .setConnectTimeout(10000) // 设置连接超时时间
                        .setSocketTimeout(60000) // 设置读数据超时时间
//                    .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY) // Cookie策略
                        .build();
        httpClientBuilder.setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG);

        httpClientBuilder.addInterceptorFirst(new RequestAcceptEncoding()); // 设置GZIP请求的支持
        httpClientBuilder.addInterceptorFirst(new ResponseContentEncoding()); // 设置GZIP响应的支持
        httpClientBuilder.setUserAgent(getRandomUserAgent()); // 设置User-Agent

        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(1, true);
        httpClientBuilder.setRetryHandler(retryHandler); // 设置重试

        Registry<ConnectionSocketFactory> registry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.INSTANCE)
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(400);
        connectionManager.setDefaultMaxPerRoute(200);

        SocketConfig defaultSocketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();
        connectionManager.setDefaultSocketConfig(defaultSocketConfig);

        // Create connection configuration
        ConnectionConfig defaultConnectionConfig =
                ConnectionConfig.custom().setCharset(Consts.UTF_8).build();
        connectionManager.setDefaultConnectionConfig(defaultConnectionConfig);

        httpClientBuilder.setConnectionManager(connectionManager);
        httpClientBuilder.setRedirectStrategy(DefaultRedirectStrategy.INSTANCE);
        HTTP_CLIENT = httpClientBuilder.build();
    }

    public static String execute(HttpUriRequest request) throws ClientProtocolException, IOException {
        return HTTP_CLIENT.execute(request, new BasicResponseHandler());
    }

    public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler) throws ClientProtocolException, IOException {
        return HTTP_CLIENT.execute(request, responseHandler);
    }

    public static String getRandomUserAgent() {
        return USER_AGENTS[RandomUtils.nextInt(0, USER_AGENTS.length)];
    }
}
