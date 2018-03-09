package com.zxk.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 唯一标示工具
 *
 * Created by wangyi on 2016/11/22.
 */
public class TraceUtil {

    //十六进制IP地址
    private static String IP_16 = "ffffffff";
    //序号,从1000开始累加
    private static AtomicInteger count = new AtomicInteger(1000);

    /**
     * 获取tranceId
     * @return
     */
    public static String getTraceId() {
        return getTraceId(IP_16, System.currentTimeMillis(), getNextId());
    }

    /**
     * 获取tranceId
     * @param ip 十六进制IP地址
     * @param timestamp 时间戳
     * @param nextId 序号
     * @return
     */
    private static String getTraceId(String ip, long timestamp, int nextId) {
        StringBuilder appender = new StringBuilder(25);
        appender.append(ip).append(timestamp).append(nextId);
        return appender.toString();
    }

    /**
     * 把IP地址转换为十六进制
     * @param ip
     * @return
     */
    private static String getIP_16(String ip) {
        String[] ips = ip.split("\\.");
        StringBuilder sb = new StringBuilder();
        String[] arr = ips;
        int len$ = ips.length;

        for (int i = 0; i < len$; ++i) {
            String column = arr[i];
            String hex = Integer.toHexString(Integer.parseInt(column));
            if (hex.length() == 1) {
                sb.append('0').append(hex);
            } else {
                sb.append(hex);
            }
        }

        return sb.toString();
    }

    /**
     * 获取本机十进制IP地址
     * @return
     */
    private static String getInetAddress() {
        try {
            Enumeration t = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;

            while (t.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) t.nextElement();
                Enumeration addresses = ni.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    address = (InetAddress) addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostAddress();
                    }
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static int getNextId() {
        int current;
        int next;
        do {
            current = count.get();
            next = current > 9000 ? 1000 : current + 1;
        } while (!count.compareAndSet(current, next));

        return next;
    }

    static {
        try {
            String e = getInetAddress();
            if (e != null) {
                IP_16 = getIP_16(e);
            }
        } catch (Exception e) {
            ;
        }

    }

}
