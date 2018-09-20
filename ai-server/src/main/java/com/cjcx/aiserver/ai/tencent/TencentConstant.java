package com.cjcx.aiserver.ai.tencent;

/**
 * 访问百度的所有接口地址
 */
public class TencentConstant {

    public static final String TAG = "Tencent";

    //================================图像OCR=============================
    private static String HOST = "http://recognition.image.myqcloud.com";

    public static String BUCKETNAME_IMAGE ="/ocr/general";

    //OCR-通用印刷体识别 50/日
    public static String url_general = HOST + BUCKETNAME_IMAGE;

}
