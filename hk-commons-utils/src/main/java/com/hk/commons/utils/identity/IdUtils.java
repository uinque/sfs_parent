package com.hk.commons.utils.identity;

import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by linhy on 2017/4/19.
 */
public class IdUtils {

    private AtomicInteger sequence = new AtomicInteger();
    private long prefix;
    private volatile long timestamp;
    private volatile int increment;

    private static IdUtils instance = new IdUtils();

    private IdUtils() {
        PropertyConfigurer pc = new PropertyConfigurer();
        String cluster = pc.get("app.cluster.id", String.valueOf(Long.parseLong(getHostIpTrim())%8)); // 集群ID
        String node = pc.get("app.node.id", String.valueOf(Long.parseLong(getCurrentPid())%256)); // 节点ID
        long clusterId = 0;
        long nodeId = 0;
        try {
            clusterId = Long.parseLong(cluster);
            if (0 > clusterId || 7 < clusterId) {  //有的没有使用unsign，建议以后都使用unsigned，才可以8改为15
                throw new Exception();
            }
        } catch (Exception e) {
            throw new RuntimeException("app.cluster.id配置参数异常[0-15]: " + cluster, e);
        }
        try {
            nodeId = Long.parseLong(node);
            if (0 > nodeId || 255 < nodeId) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new RuntimeException("app.node.id配置参数异常[0-255]: " + node, e);
        }

        prefix = ((clusterId & 0x0F) << 60) + ((nodeId & 0xFF) << 52);
        prefix &= 0xFFF0000000000000L;
    }

    public static long id() {
        long timestamp = (((System.currentTimeMillis() / 1000) & 0xFFFFFFFFL) << 20) & 0x0FFFFFFFF00000L;
        int increment = instance.sequence.getAndIncrement() & 0x0FFFFF;

        if(timestamp <= instance.timestamp) {
            while(timestamp < instance.timestamp) {
                timestamp += 0x100000L;
            }
            if(increment == instance.increment) {
                timestamp += 0x100000L;
            }
        } else {
            instance.increment = increment;
        }
        instance.timestamp = timestamp;

        return instance.prefix + timestamp + increment;
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取当前进程号
     * @return
     */
    private static String getCurrentPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return StringUtils.split(name, "@")[0];
    }

    /**
     * 获取本机IP第四段
     * @return
     */
    private static String getHostIpTrim() {
        InetAddress ia = null;
        String result = String.valueOf(Long.parseLong(getCurrentPid())%16);
        try {
            ia = InetAddress.getLocalHost();
            String hostIp = ia.getHostAddress();
            if (StringUtils.isNotBlank(hostIp)) {
                result = StringUtils.split(hostIp, ".")[3];
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Properties配置文件解析类.
     *
     * @author linjx
     * @Date 2016年6月21日
     */
    private class PropertyConfigurer {

        ResourceBundle rb;

        /**
         * 默认读取应用类路径根目录下的app-config.properties配置文件.
         */
        public PropertyConfigurer() {
            try {
                rb = ResourceBundle.getBundle("app-config");
            } catch (Exception e) {
                rb = null;
            }
        }

        /**
         * Gets a resource bundle using the specified base name.
         * @param baseName the base name of the resource bundle, a fully qualified class name
         */
        public PropertyConfigurer(String baseName) {
            try {
                rb = ResourceBundle.getBundle(baseName);
            } catch (Exception e) {
                rb = null;
            }
        }

        public String get(String key) {
            if (null == rb) {
                return null;
            }
            try{
                return rb.getString(key);
            } catch (Exception e) {
            }
            return null;
        }

        public String get(String key, String defaultValue) {
            String val = get(key);
            return StringUtils.isBlank(val) ? defaultValue : val;
        }

    }

}
