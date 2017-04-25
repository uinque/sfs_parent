package com.hk.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 操作系统工具类
 * Created by linhy on 2017/4/20.
 */
public class OsUtils {

    private final static Logger logger = LoggerFactory.getLogger(OsUtils.class);
    private static String pid;		//进程号（获取后缓存）
    private static String hostIp;	//本机ip（获取后缓存）
//	/**
//     * 单网卡名称
//     */
//    private static final String NETWORK_CARD = "eth0";
//
//    /**
//     * 绑定网卡名称
//     */
//    private static final String NETWORK_CARD_BAND = "bond0";

    /**
     * 获取当前进程号
     * @return
     */
    public static String getCurrentPid() {
        if(StringUtils.isBlank(pid)) {
            try {
                String name = ManagementFactory.getRuntimeMXBean().getName();
                pid = StringUtils.split(name, "@")[0];
            } catch (Exception e) {
                logger.error("获取当前进程号异常", e);
            }
        }
        return pid;
    }

    /**
     * 获取机器hostname
     * @return
     * @throws Exception
     */
    public static String getHostName() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            return hostname;
        } catch (UnknownHostException e) {
            throw new RuntimeException("获取hostname失败");
        }
    }

    /**
     * 获取本机ip
     * @return
     * @throws Exception
     */
    public static String getHostIp() {
        try {
            if(StringUtils.isBlank(hostIp)) {
                InetAddress addr = InetAddress.getLocalHost();
                String ip = addr.getHostAddress();
                if(ip != null && ip.equals("127.0.0.1")) {
                    logger.warn("从host获取ip得到127.0.0.1，将从网卡信息重新获取......");
                    return getRealHostIp();
                }
                hostIp = ip;
            }
            return hostIp;
        } catch (Exception e) {
            throw new RuntimeException("获取本机ip失败", e);
        }
    }

    /**
     * 获取本机ip
     * @return
     * @throws Exception
     */
    public static String getRealHostIp() {
        if(StringUtils.isNotBlank(hostIp)) return hostIp;
        try {
            Enumeration<NetworkInterface> e1 = (Enumeration<NetworkInterface>)NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = e1.nextElement();
                Enumeration<InetAddress> e2 = ni.getInetAddresses();
                //获取第一个网卡地址
                while (e2.hasMoreElements()) {
                    InetAddress ia = e2.nextElement();
                    if(ia.isSiteLocalAddress()) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
                break;
//                //单网卡或者绑定双网卡
//                if ((NETWORK_CARD.equals(ni.getName()))
//                    || (NETWORK_CARD_BAND.equals(ni.getName()))) {
//                    Enumeration<InetAddress> e2 = ni.getInetAddresses();
//                    while (e2.hasMoreElements())
//                    {
//                        InetAddress ia = e2.nextElement();
//                        if (ia instanceof Inet6Address)
//                        {
//                            continue;
//                        }
//                        hostIp = ia.getHostAddress();
//                    }
//                    break;
//                }
//                else
//                {
//                    continue;
//                }
            }
        }
        catch (SocketException e) {
            throw new RuntimeException("获取本机ip失败", e);
        }
        return hostIp;
    }
}
