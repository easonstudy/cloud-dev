package com.lottery.center.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Http 请求工具
 * guo
 */
@SuppressWarnings("unchecked")
public class HttptUtils {

    private static Logger log = LoggerFactory.getLogger(HttptUtils.class);

    //请求方式
    private static String METHOD_GET = "GET";
    private static String METHOD_POST = "POST";
    private static String METHOD_PUT = "PUT";
    private static String METHOD_DELETE = "DELETE";


    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        try {
            String result = null;
            result = doGet("http://www.xxxx.com", null);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param url       请求url
     * @param params    请求参数 a=1&b=2&c=3 字符串
     * @return
     * @throws Exception
     */
    public static String doGet(String url, String params) throws Exception {
        return doGet(url, params, null);
    }

    /**
     * @param url       请求url
     * @param params    请求参数 a=1&b=2&c=3 字符串
     * @param headerMap 请求头
     * @return
     */
    public static String doGet(String url, String params, Map<String, String> headerMap) {
        String result = "";
        BufferedReader in = null;
        try {

            String urlNameString = url + (params != null ? ("?" + params) : "");
            URL realUrl = new URL(urlNameString);
            // 建立新的连接实例
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

            // 设置 通用请求属性
            conn = setDefaultRequestProperty(conn);
            // 设置 扩展请求属性
            conn = setExtensionRequestProperty(conn, headerMap);

            // 建立实际的连接
            conn.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.info("发送GET请求出现异常！" + e);
            e.printStackTrace();
            result = null;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @param url     请求url
     * @param params  请求参数 a=1&b=2&c=3 字符串
     * @return
     */
    public static String doPost(String url, String params) {
        return doPost(url, params, null);
    }

    /**
     * @param url    请求url
     * @param map    请求参数(Map集合)
     * @return
     */
    public static String doPost(String url, Map<String, Object> map) {
        return doPost(url, getParamsByMap(map), null);
    }


    /**
     * @param url       请求url
     * @param params    请求参数 a=1&b=2&c=3 字符串
     * @param headerMap 请求头
     * @return
     */
    public static String doPost(String url, String params, Map<String, String> headerMap) {
        log.info("Post Url:{}", url);
        log.info("Post Params:{}", params);
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(METHOD_POST); // POST方法

            // 设置 通用请求属性
            conn = setDefaultRequestProperty(conn);
            // 设置 扩展请求属性
            conn = setExtensionRequestProperty(conn, headerMap);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            if (params != null) {
                // 发送请求参数
                out.write(params);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.info("发送 POST 请求出现异常！" + e);
            e.printStackTrace();

        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //log.info("发送POST请求结果: " + result);
        return result;
    }


    public static String doPostByJson(String url, String params) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Content-Type", "application/json;charset=utf-8");
        return doPost(url, params, map);
    }


    /**
     * 设置 默认 请求头 属性
     *
     * @param conn
     * @return
     */
    private static HttpURLConnection setDefaultRequestProperty(HttpURLConnection conn) {
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        return conn;
    }

    /**
     * 设置 扩展 请求头 属性
     *
     * @param conn
     * @return
     */
    private static HttpURLConnection setExtensionRequestProperty(HttpURLConnection conn, Map<String, String> headerMap) {
        if (headerMap != null && headerMap.size() > 0) {
            for (Entry<String, String> o : headerMap.entrySet()) {
                conn.setRequestProperty(o.getKey(), o.getValue());
            }
        }
        return conn;
    }


    /**
     * url(aa=11&bb=22&cc=33) 转 map
     *
     * @param params
     * @return
     */
    public static Map<String, Object> getMapByParams(String params) {
        Map<String, Object> map = new HashMap<String, Object>(0);
        if (StringUtils.isBlank(params)) {
            return map;
        }
        String[] arr = params.split("&");
        for (int i = 0; i < arr.length; i++) {
            String[] p = arr[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * map 转 url(aa=11&bb=22&cc=33)
     *
     * @param map
     * @return
     */
    public static String getParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

}
