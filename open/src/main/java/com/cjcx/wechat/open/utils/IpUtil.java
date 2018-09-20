package com.cjcx.wechat.open.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP工具类
 */
public class IpUtil {
    /**
     * 获得IP
     */
    public static String getIp(HttpServletRequest request) {
        String s = request.getHeader("CF-Connecting-IP");
        if (s != null && !"".equals(s.trim()))
            return s;
        s = request.getHeader("X-Forwarded-For");
        if (s != null && !"".equals(s.trim()))
            return s;
        return request.getRemoteAddr();
    }

    public static String getLocalIp() {
        URL url = null;
        try {
            url = new URL("http://www.ip138.com/ip2city.asp");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedInputStream bis;
        try {
            bis = new BufferedInputStream(url.openStream());
            InputStreamReader isr = new InputStreamReader(bis);
            BufferedReader reader = new BufferedReader(isr);
            String s = null;
            while ((s = reader.readLine()) != null) {
                if (s.trim().equals(""))
                    continue;
                String regEx = "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\." +
                        "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\." +
                        "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\." +
                        "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])";
                Pattern pat = Pattern.compile(regEx);
                Matcher mat = pat.matcher(s);
                boolean rs = mat.find();
                if (rs)
                    return mat.group(0);
            }
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "0:0:0:0:1";
        }
        return null;
    }

    /**
     * 获取本机MAC地址
     *
     * @return
     * @throws SocketException
     * @throws UnknownHostException
     */
    public static String getLocalMac() throws SocketException, UnknownHostException {
        InetAddress ia = InetAddress.getLocalHost();

        //获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        //System.out.println("mac数组长度：" + mac.length);
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            //System.out.println("每8位:"+str);
            if (str.length() == 1) {
                sb.append("0" + str);
            } else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }

}
