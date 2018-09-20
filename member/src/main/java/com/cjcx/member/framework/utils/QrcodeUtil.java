package com.cjcx.member.framework.utils;

import com.cjcx.member.framework.zxing.MatrixToImageWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

public class QrcodeUtil {

    /**
     * 生成二维码图片 流
     *
     * @param text 内容
     * @return 流
     */
    public static InputStream getQrcodeInputStream(String text) {
        InputStream inputStream = null;
        try {
            int width = 512;
            int height = 512;
            // 二维码的图片格式
            String format = "png";
            /**
             * 设置二维码的参数
             */
            HashMap hints = new HashMap();
            // 内容所使用编码
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            inputStream = MatrixToImageWriter.bufferImageToInputStream(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }


    /**
     * 二维码的生成
     */
    public static void createQrImage(String filePath, String text) {
        int width = 512;
        int height = 512;
        // 二维码的图片格式
        String format = "png";
        /**
         * 设置二维码的参数
         */
        HashMap hints = new HashMap();
        // 内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            // 生成二维码
            File outputFile = new File(filePath);

            MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
