package com.cjcx.aiserver.ai.utils;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图形处理工具类
 */
@Slf4j
public class ImageUtils {

    /**
     * 网络图片 转 流
     *
     * @param newworkImageUrl
     * @return 流
     */
    public static InputStream getInputStreamByNetworkImageUrl(String newworkImageUrl) {
        try {
            URL url = new URL(newworkImageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            return conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 流 转 Base64字符串
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String getBase64ByInputStream(InputStream inputStream) throws Exception {
        byte[] data = getBytesByInputStream(inputStream);
        log.info("Base64 图像字节长度:{}", data.length);
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);

    }

    /**
     * 网络图片 转  Base64字符串
     *
     * @return 字符串
     * @Description: 根据图片inputStream转换为base64编码字符串
     * @Author:
     * @CreateTime:
     */
    public static String getBase64ByNetworkImageUrl(String newworkImageUrl) throws Exception {
        return getBase64ByInputStream(getInputStreamByNetworkImageUrl(newworkImageUrl));
    }


    /**
     * 流 转 字节
     *
     * @param inStream
     * @return 字节
     * @throws Exception
     */
    public static byte[] getBytesByInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
