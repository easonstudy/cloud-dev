package com.cjcx.aiserver.ai.baidu;

import com.cjcx.aiserver.ai.AbstractImageHandler;
import com.cjcx.aiserver.ai.config.AbstractConfig;
import com.cjcx.aiserver.ai.config.ImageConfig;
import com.cjcx.aiserver.ai.baidu.entity.WordsResponse;
import com.cjcx.aiserver.ai.baidu.entity.WordsResult;
import com.cjcx.aiserver.ai.utils.HttptUtils;
import com.cjcx.aiserver.ai.utils.ImageUtils;
import com.cjcx.aiserver.obj.ResultObject;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 图形处理类
 */
@Slf4j
@NoArgsConstructor
@Component
public class BaiduImageHandler extends AbstractImageHandler {

    /**
     * // 基础 50000次/日
     * // String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
     * // 位置信息 500次/日
     * // String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general";
     * // 高精度 500次/日
     * // String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
     * // 高精度&位置信息 50/日
     * // String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate";
     * // 含偏生字 无
     * // "https://aip.baidubce.com/rest/2.0/ocr/v1/general_enhanced";
     */

    /**
     * @param url 图像url
     * @return 分析后的内容数据
     */
    public String getContentByUrl(String url) {
        long s = System.currentTimeMillis();
        try {
            // 获取图片流
            InputStream inputStream = ImageUtils.getInputStreamByNetworkImageUrl(url);
            //转成base64加密串
            String base64 = ImageUtils.getBase64ByInputStream(inputStream);
            log.info("Baidu 图片转Base64用时:{}", (System.currentTimeMillis() - s));
            base64 = URLEncoder.encode(base64, "UTF-8");
            return getContentByImage(base64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param base64 图像image
     * @return
     */
    public String getContentByImage(String base64) {
        String result = null;
        String request_address = null;


        String apiKey = null;
        try {
            apiKey = BaiduAccountManager.getImageDefaultApiKey();
            if (apiKey == null) {
                log.error("百度 获取当前apiKey为空");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("access_token", BaiduTokenManager.getToken(apiKey));
            map.put("image", base64);
            map.put("detect_direction", true);
            String params = HttptUtils.getParamsByMap(map);

            switch (imageConfig.getLevel()) {
                case 1:
                    request_address = BaiduInterface.url_accurate_basic;
                    break;
                case 2:
                    request_address = BaiduInterface.url_accurate;
                    break;
                default:
                    request_address = BaiduInterface.general_basic;
                    break;
            }
            log.info("Baidu 请求地址:{}", request_address);
            result = HttptUtils.doPost(request_address, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //本地缓存更新
            limit(apiKey, request_address);
        }
        return result;
    }


    @Override
    public ResultObject execute(AbstractConfig config) {
        ResultObject r = new ResultObject();
        imageConfig = (ImageConfig)config;

        String content = null;
        if (StringUtils.hasText(imageConfig.getImage())) {
            log.info("Baidu 请求参数 Base64:{}", imageConfig.getImage());
            content = this.getContentByImage(imageConfig.getImage());
        } else if (StringUtils.hasText(imageConfig.getUrl())) {
            log.info("Baidu 请求参数 图像地址:{}", imageConfig.getUrl());
            content = this.getContentByUrl(imageConfig.getUrl());
        } else {
            r.setErrCode("-1");
            r.setMsg("缺少请求参数");
        }

        log.info("Baidu 识别类型:{}, 结果:{}", imageConfig.getLevel(), content);
        if (StringUtils.isEmpty(content)) {
            r.setErrCode("-1");
            r.setMsg("未能解析出小票内容");
            return r;
        }

        //识别之后执行处理
        afterExecute(content, r);

        return r;
    }

    /**
     * 处理 返回的结果
     *
     * @param content
     * @param r
     * @return
     */
    @Override
    protected void afterExecute(String content, ResultObject r) {
        Gson gson = new Gson();
        WordsResponse wr = gson.fromJson(content, WordsResponse.class);
        if (wr.getError_code() != 0) {
            r.setErrCode(wr.getError_code().toString());
            r.setMsg(wr.getError_msg());
            return;
        }

        List<WordsResult> list = wr.getWords_result();

        StringBuffer result = new StringBuffer();
        if (list != null) {

            //无位置信息 (普通, 高精度)
            if (imageConfig.getLevel() == 0 || imageConfig.getLevel() == 1) {
                StringBuffer buffer = new StringBuffer();
                WordsResult words = null;
                for (int i = 0; i < list.size(); i++) {
                    words = list.get(i);
                    buffer.append(words.getWords()).append("\r\n");
                }
                result = buffer;
            }

            //有位置信息 (高精度)
            if (imageConfig.getLevel() == 2) {
                Map<Integer, String> map = new HashMap<>();
                List<Integer> tops = new ArrayList<>();
                WordsResult words = null;
                for (int i = 0; i < list.size(); i++) {
                    words = list.get(i);
                    tops.add(words.getLocation().getTop());
                    map.put(words.getLocation().getTop(), words.getWords());
                }

                Collections.sort(tops);          //排序(默认升序)
                //System.out.println("===================处理前======================");
                //final StringBuffer r = new StringBuffer();
                //tops.stream().map((e) -> map.get(e)).forEach((e) -> r.append(e).append("\r\n"));
                //System.out.println(r);
                //r.setLength(0);

                Integer pre = 0;
                for (int i = 0; i < tops.size(); i++) {
                    Integer now = tops.get(i);
                    if (i >= 1) {
                        if (now <= pre + FAULT_TOLERANT_VALUE) {
                            //在容错范围内, 即在同一行
                            String nowLine = map.get(now);           //当前行的值
                            map.put(pre, map.get(pre) + nowLine);
                            map.remove(now);
                        }
                    }
                    pre = now;
                }

                tops.clear();
                for (Map.Entry<Integer, String> o : map.entrySet()) {
                    tops.add(o.getKey());
                }
                Collections.sort(tops);
                for (int i = 0; i < tops.size(); i++) {
                    result.append(map.get(tops.get(i))).append("\r\n");
                }
                //System.out.println("===================处理后======================");
                //System.out.println(result.toString());
            }
        }

        r.setData(result.toString());
    }


    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        ImageConfig config = new ImageConfig();
        config.setLevel(2);

        BaiduImageHandler ha = new BaiduImageHandler();
        ha.setImageConfig(config);

        String content = "{\"log_id\": 5787545109874569576, \"direction\": 0, \"words_result_num\": 42, \"words_result\": [{\"location\": {\"width\": 247, \"top\": 0, \"height\": 28, \"left\": 66}, \"words\": \"蕾莎三楼都市丽人专卖店\"}, {\"location\": {\"width\": 77, \"top\": 24, \"height\": 20, \"left\": 153}, \"words\": \"300A108\"}, {\"location\": {\"width\": 141, \"top\": 44, \"height\": 28, \"left\": 0}, \"words\": \"收银机号:001\"}, {\"location\": {\"width\": 66, \"top\": 67, \"height\": 27, \"left\": 0}, \"words\": \"收银员\"}, {\"location\": {\"width\": 53, \"top\": 71, \"height\": 22, \"left\": 271}, \"words\": \"(顾客\"}, {\"location\": {\"width\": 127, \"top\": 99, \"height\": 20, \"left\": 0}, \"words\": \"号:300A10801\"}, {\"location\": {\"width\": 34, \"top\": 98, \"height\": 23, \"left\": 288}, \"words\": \"联)\"}, {\"location\": {\"width\": 197, \"top\": 119, \"height\": 25, \"left\": 0}, \"words\": \"业务员号:300A10803\"}, {\"location\": {\"width\": 163, \"top\": 143, \"height\": 26, \"left\": 0}, \"words\": \"交易流水:121969\"}, {\"location\": {\"width\": 293, \"top\": 167, \"height\": 22, \"left\": 0}, \"words\": \"交易时间:2018-08-0610:43:15\"}, {\"location\": {\"width\": 220, \"top\": 210, \"height\": 25, \"left\": 0}, \"words\": \"数量单价折扣\"}, {\"location\": {\"width\": 51, \"top\": 210, \"height\": 26, \"left\": 309}, \"words\": \"金额\"}, {\"location\": {\"width\": 141, \"top\": 255, \"height\": 23, \"left\": 0}, \"words\": \"1.都市缤纷派内衣\"}, {\"location\": {\"width\": 138, \"top\": 258, \"height\": 21, \"left\": 242}, \"words\": \"6901863864508\"}, {\"location\": {\"width\": 219, \"top\": 283, \"height\": 21, \"left\": 0}, \"words\": \"1.059.000.00\"}, {\"location\": {\"width\": 51, \"top\": 283, \"height\": 19, \"left\": 301}, \"words\": \"59.00\"}, {\"location\": {\"width\": 142, \"top\": 302, \"height\": 21, \"left\": 0}, \"words\": \"2.都市缤纷派内衣\"}, {\"location\": {\"width\": 141, \"top\": 305, \"height\": 21, \"left\": 242}, \"words\": \"6901863863754\"}, {\"location\": {\"width\": 219, \"top\": 329, \"height\": 20, \"left\": 0}, \"words\": \"1.039.000.00\"}, {\"location\": {\"width\": 50, \"top\": 330, \"height\": 19, \"left\": 301}, \"words\": \"39.0\"}, {\"location\": {\"width\": 92, \"top\": 372, \"height\": 27, \"left\": 0}, \"words\": \"零售金额\"}, {\"location\": {\"width\": 77, \"top\": 374, \"height\": 23, \"left\": 247}, \"words\": \"98.00元\"}, {\"location\": {\"width\": 89, \"top\": 396, \"height\": 25, \"left\": 0}, \"words\": \"总折扣\"}, {\"location\": {\"width\": 66, \"top\": 400, \"height\": 20, \"left\": 259}, \"words\": \"0.00元\"}, {\"location\": {\"width\": 100, \"top\": 421, \"height\": 22, \"left\": 0}, \"words\": \"总数量:\"}, {\"location\": {\"width\": 49, \"top\": 422, \"height\": 20, \"left\": 271}, \"words\": \"2.0件\"}, {\"location\": {\"width\": 90, \"top\": 442, \"height\": 26, \"left\": 0}, \"words\": \"应收\"}, {\"location\": {\"width\": 76, \"top\": 445, \"height\": 20, \"left\": 248}, \"words\": \"98.00元\"}, {\"location\": {\"width\": 100, \"top\": 465, \"height\": 24, \"left\": 0}, \"words\": \"人民币:\"}, {\"location\": {\"width\": 73, \"top\": 467, \"height\": 22, \"left\": 248}, \"words\": \"98.00元\"}, {\"location\": {\"width\": 90, \"top\": 488, \"height\": 25, \"left\": 0}, \"words\": \"实收\"}, {\"location\": {\"width\": 85, \"top\": 492, \"height\": 21, \"left\": 239}, \"words\": \"100.00元\"}, {\"location\": {\"width\": 90, \"top\": 511, \"height\": 26, \"left\": 0}, \"words\": \"找零\"}, {\"location\": {\"width\": 63, \"top\": 515, \"height\": 20, \"left\": 259}, \"words\": \"2.00元\"}, {\"location\": {\"width\": 132, \"top\": 557, \"height\": 28, \"left\": 124}, \"words\": \"欢迎光临\"}, {\"location\": {\"width\": 257, \"top\": 608, \"height\": 27, \"left\": 62}, \"words\": \"客服热线:400-88653888\"}, {\"location\": {\"width\": 347, \"top\": 632, \"height\": 25, \"left\": 0}, \"words\": \"尊敬的顾客:欢迎选购都市丽人产\"}, {\"location\": {\"width\": 371, \"top\": 659, \"height\": 27, \"left\": 0}, \"words\": \"品,由于内衣裤,秋衣,保暖属于私\"}, {\"location\": {\"width\": 371, \"top\": 687, \"height\": 25, \"left\": 0}, \"words\": \"密贴身衣物涉及个人卫生,如非质量\"}, {\"location\": {\"width\": 372, \"top\": 714, \"height\": 26, \"left\": 0}, \"words\": \"问题一经出售概不退换。睡衣号码不\"}, {\"location\": {\"width\": 372, \"top\": 741, \"height\": 28, \"left\": 0}, \"words\": \"合适,三天之内,不拆牌,可凭小票\"}, {\"location\": {\"width\": 198, \"top\": 770, \"height\": 26, \"left\": 0}, \"words\": \"调换号,谢谢合作!\"}]}";


        ResultObject r = new ResultObject();
        ha.afterExecute(content, r);

        System.out.println(r.toString());
    }
}
