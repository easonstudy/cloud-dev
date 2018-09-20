package com.cjcx.aiserver.ai.baidu;

import java.util.HashMap;
import java.util.Map;

/**
 * 访问百度的所有接口地址
 */
public class BaiduInterface {

    //================================图像OCR=============================
    //基础 50000次/日
    public static String general_basic = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";

    //高精度 500次/日
    public static String url_accurate_basic = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";

    //高精度&位置信息 50/日
    public static String url_accurate = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate";

}
