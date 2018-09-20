package com.cjcx.aiserver;

import com.cjcx.aiserver.ai.baidu.entity.WordsResponse;
import com.cjcx.aiserver.ai.baidu.entity.WordsResult;
import com.cjcx.aiserver.ai.utils.HttptUtils;
import com.cjcx.aiserver.ai.utils.ImageUtils;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestImageOcr {
    static String url_basic = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";

    static String url_accurate_basic = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";

    public static void main(String[] args) {
        String access_token = "24.c98b9f4e06051c5fed5d02b9ad723fdd.2592000.1533292872.282335-11324883";
        System.out.println("access_token:" + access_token);
        try {
            long s = System.currentTimeMillis();
            //网络图片  二进制流
            //String netUrl = "http://android-pos.cn/images/receipt_audit/b1f7d5ea-d914-4041-865f-e7a757b4e462.jpg";
            //String netUrl = "http://android-pos.cn/images/receipt_audit/0f352419-a6b4-4011-8771-bfdbd9898825.png";
            String netUrl = "http://www.yunpiaobox.cn:18080/mallimage/00000594_20180706110731144.png";
            //String result = AbstractImageHandler.getContentByUrl(netUrl);
            String base64 = ImageUtils.getBase64ByNetworkImageUrl(netUrl);
            System.out.println(" base64用时:" + (System.currentTimeMillis() - s));

            Map<String, Object> map = new HashMap<>();
            map.put("access_token", access_token);
            map.put("image", URLEncoder.encode(base64, "UTF-8"));
            map.put("detect_direction", true);
            String params = HttptUtils.getParamsByMap(map);
            //String result = HttptUtils.doPost(url_basic, params);
            String result = HttptUtils.doPost(url_accurate_basic, params);
            System.out.println("百度 结果:" + result);

            Gson gson = new Gson();
            WordsResponse wr = gson.fromJson(result, WordsResponse.class);
            if (wr.getError_code() == 0) {
                List<WordsResult> list = wr.getWords_result();
                if (list != null) {
                    StringBuffer buffer = new StringBuffer();
                    WordsResult words = null;
                    for (int i = 0; i < list.size(); i++) {
                        words = list.get(i);
                        buffer.append(words.getWords()).append("\r\n");
                    }
                    result = buffer.toString();
                }
            }
            System.out.println("百度 格式化后结果:" + result);
            System.out.println(" 总用时:" + (System.currentTimeMillis() - s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
