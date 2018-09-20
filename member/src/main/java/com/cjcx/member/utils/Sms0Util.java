package com.cjcx.member.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * 短信网
 序号	名称	        短信条数	    元/条	费用	        介绍
 1		5000元套餐	90000	    0.056	5000元	    5000元套餐
 2		20000元套餐	385000	    0.052	20000元	    20000元套餐
 3		50000元套餐	1000000	    0.05	50000元	    50000元套餐
 4		100000元套餐	2220000	    0.045	100000元	    100000元套餐
 5		200000元套餐	5000000	    0.04	200000元 	200000元套餐
 6		500000元套餐	13100000	0.038	500000元	    500000元套餐
 */
public class Sms0Util {

    public static void main(String[] args) throws Exception {
        // 用户名
        String name="wbxxx";
        // 密码
        String pwd="0C759A360WWBD5F5E0F5FF9F0597";
        // 电话号码字符串，中间用英文逗号间隔
        StringBuffer mobileString=new StringBuffer("");
        // 内容字符串
        StringBuffer contextString=new StringBuffer("短信内容");
        // 签名
        String sign="签名";
        // 追加发送时间，可为空，为空为及时发送
        String stime="";
        // 扩展码，必须为数字 可为空
        StringBuffer extno=new StringBuffer();

        System.out.println(doPost(name, pwd, mobileString, contextString, sign, stime, extno));
    }

    /**
     * 发送短信
     *
     * @param name			用户名
     * @param pwd			密码
     * @param mobileString	电话号码字符串，中间用英文逗号间隔
     * @param contextString	内容字符串
     * @param sign			签名
     * @param stime			追加发送时间，可为空，为空为及时发送
     * @param extno			扩展码，必须为数字 可为空
     * @return
     * @throws Exception
     */
    public static String doPost(String name, String pwd,
                                StringBuffer mobileString, StringBuffer contextString,
                                String sign, String stime, StringBuffer extno) throws Exception {
        StringBuffer param = new StringBuffer();
        param.append("name="+name);
        param.append("&pwd="+pwd);
        param.append("&mobile=").append(mobileString);
        param.append("&content=").append(URLEncoder.encode(contextString.toString(),"UTF-8"));
        param.append("&stime="+stime);
        param.append("&sign=").append(URLEncoder.encode(sign,"UTF-8"));
        param.append("&type=pt");
        param.append("&extno=").append(extno);

        URL localURL = new URL("联系客服获取接口请求地址");
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));

        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String resultBuffer = "";

        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);

            outputStreamWriter.write(param.toString());
            outputStreamWriter.flush();

            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            inputStream = httpURLConnection.getInputStream();
            resultBuffer = convertStreamToString(inputStream);

        } finally {

            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }

        return resultBuffer;
    }


    /**
     * 转换返回值类型为UTF-8格式.
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        StringBuilder sb1 = new StringBuilder();
        byte[] bytes = new byte[4096];
        int size = 0;

        try {
            while ((size = is.read(bytes)) > 0) {
                String str = new String(bytes, 0, size, "UTF-8");
                sb1.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb1.toString();
    }


}
